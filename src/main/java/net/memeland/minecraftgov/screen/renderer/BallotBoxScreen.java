package net.memeland.minecraftgov.screen.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.data.PartyData;
import net.memeland.minecraftgov.networking.ModMessages;
import net.memeland.minecraftgov.networking.packet.VotePacket;
import net.memeland.minecraftgov.networking.packet.VoteDataHolder;
import net.memeland.minecraftgov.screen.BallotBoxMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BallotBoxScreen extends AbstractContainerScreen<BallotBoxMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/gui/ballot_box_gui.png");

    private static final ResourceLocation[] SYMBOL_TEXTURES = new ResourceLocation[25];

    static {
        for (int i = 0; i < 25; i++) {
            SYMBOL_TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "textures/misc/symbols/symbol" + (i + 1) + ".png");
        }
    }

    private List<PartyData> availableParties = new ArrayList<>();
    private Map<String, Integer> voteData = new java.util.HashMap<>();
    private boolean hasPlayerVoted = false;
    private int totalVotes = 0;
    private final List<Button> voteButtons = new ArrayList<>();

    public BallotBoxScreen(BallotBoxMenu ballotBoxMenu, Inventory inventory, Component component) {
        super(ballotBoxMenu, inventory, component);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();

        VoteDataHolder.VoteData storedData = VoteDataHolder.getVoteData(menu.getBlockPos());
        if (storedData != null) {
            receiveVoteData(storedData.parties, storedData.votes, storedData.hasVoted, storedData.totalVotes);
            VoteDataHolder.clearVoteData(menu.getBlockPos());
        } else {
            createVoteButtons();
        }
    }

    private void loadVoteData() {
        // This method is now replaced by receiveVoteData() called from network packet
        // Keeping it for compatibility but it won't work properly on client side
    }

    public void receiveVoteData(List<PartyData> parties, Map<String, Integer> votes, boolean playerVoted, int total) {

        // Add validation
        if (parties == null || votes == null) {
            return;
        }

        // Validate parties list size
        if (parties.size() > 8) {
            return;
        }

        // Validate vote counts are non-negative
        for (Integer voteCount : votes.values()) {
            if (voteCount < 0) {
                return;
            }
        }

        // Validate total matches sum
        int calculatedTotal = votes.values().stream().mapToInt(Integer::intValue).sum();
        if (total != calculatedTotal) {
            return;
        }

        this.availableParties = new ArrayList<>(parties);
        this.voteData = new java.util.HashMap<>(votes);
        this.hasPlayerVoted = playerVoted;
        this.totalVotes = total;

        // Clear existing buttons and recreate with new data
        voteButtons.forEach(this::removeWidget);
        createVoteButtons();
    }

    private void createVoteButtons() {
        voteButtons.clear();


        if (availableParties.isEmpty()) {
            return;
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int startY = y + 52;
        int buttonWidth = 50;
        int buttonHeight = 20;
        int spacing = 22;
        int maxY = y + 215;

        for (int i = 0; i < availableParties.size() && startY + (i * spacing) < maxY; i++) {
            PartyData party = availableParties.get(i);
            int buttonY = startY + (i * spacing);

            // Vote button for this party
            Button voteButton = new Button(
                    x + 13, buttonY, buttonWidth, buttonHeight,
                    Component.translatable("gui.modgov.vote"),
                    button -> castVote(party.getName()),
                    (button, poseStack, mouseX, mouseY) -> {
                        this.renderTooltip(poseStack, Component.literal(party.getName()), mouseX, mouseY);
                    }
            );

            // Disable button if player already voted
            voteButton.active = !hasPlayerVoted;

            this.addRenderableWidget(voteButton);
            voteButtons.add(voteButton);
        }

    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);

        this.font.draw(poseStack, Component.translatable("gui.modgov.ballot_slot"), 85, 6, 0x404040);
        this.font.draw(poseStack, Component.translatable("gui.modgov.id_card_slot"), 128, 6, 0x404040);

        // Draw instruction text if not voted
        if (!hasPlayerVoted) {
            Component instructionText = Component.translatable("gui.modgov.vote_instruction");
            int instructionWidth = this.font.width(instructionText);
            this.font.draw(poseStack, instructionText, (imageWidth - instructionWidth) / 2, 40, 0x666666);
        }

        if (availableParties.isEmpty()) {
            // Show "No parties registered" message
            Component noPartiesMsg = Component.translatable("gui.modgov.no_parties");
            int msgWidth = this.font.width(noPartiesMsg);
            this.font.draw(poseStack, noPartiesMsg, (imageWidth - msgWidth) / 2, 85, 0x666666);
            return;
        }

        // Show voting status if player has voted
        if (hasPlayerVoted) {
            Component votedMsg = Component.translatable("gui.modgov.already_voted");
            int msgWidth = this.font.width(votedMsg);
            this.font.draw(poseStack, votedMsg, (imageWidth - msgWidth) / 2, 40, 0x999999);
        }

        // Draw party list with percentages
        int startY = 52;
        int spacing = 22;
        int maxY = 215;

        for (int i = 0; i < availableParties.size() && startY + (i * spacing) < maxY; i++) {
            PartyData party = availableParties.get(i);
            int labelY = startY + (i * spacing);

            int symbolX = 67;
            int symbolY = labelY + 1;

            if (party.getSymbolId() >= 1 && party.getSymbolId() <= 25) {
                RenderSystem.setShaderTexture(0, SYMBOL_TEXTURES[party.getSymbolId() - 1]);
                blit(poseStack, symbolX, symbolY, 16, 16, 0, 0, 16, 16, 16, 16);
                RenderSystem.setShaderTexture(0, TEXTURE); // Reset texture
            }

            // Calculate percentage
            double percentage = totalVotes > 0 ? (double) voteData.getOrDefault(party.getName(), 0) / totalVotes * 100.0 : 0.0;

            // Truncate party name if too long
            String displayName = party.getName();
            if (displayName.length() > 25) {
                displayName = displayName.substring(0, 22) + "...";
            }

            // Display text with percentage
            String displayText = String.format("%s: %.0f%%", displayName, percentage);

            // Get party color
            int textColor = party.getColor().getTextColor();
            if (textColor == 0xF9FFFE) { // White is hard to read
                textColor = 0x404040;
            }

            // Draw the text next to symbol
            this.font.draw(poseStack, displayText, 87, labelY + 5, textColor);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);

        if (hasShiftDown() && totalVotes > 0) {
            renderVoteCountTooltips(poseStack, mouseX, mouseY);
        }
    }

    private void renderVoteCountTooltips(PoseStack poseStack, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int startY = y + 52;
        int spacing = 22;
        int maxY = y + 215;

        for (int i = 0; i < availableParties.size() && startY + (i * spacing) < maxY; i++) {
            PartyData party = availableParties.get(i);
            int labelY = startY + (i * spacing);

            if (mouseX >= x + 65 && mouseX <= x + imageWidth - 23 &&
                    mouseY >= labelY && mouseY <= labelY + 16) {

                int voteCount = voteData.getOrDefault(party.getName(), 0);
                Component tooltip = Component.literal(String.format("%d votes", voteCount));
                this.renderTooltip(poseStack, tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    private void castVote(String partyName) {
        if (!hasPlayerVoted) {
            VotePacket packet = new VotePacket(menu.getBlockPos(), partyName);
            ModMessages.sendToServer(packet);

            this.onClose();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}