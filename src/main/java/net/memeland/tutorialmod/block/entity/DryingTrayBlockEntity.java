package net.memeland.tutorialmod.block.entity;

import net.memeland.tutorialmod.block.custom.DryingTrayBlock;
import net.memeland.tutorialmod.fluid.ModFluids;
import net.memeland.tutorialmod.item.ModItems;
import net.memeland.tutorialmod.networking.ModMessages;
import net.memeland.tutorialmod.networking.packet.EnergySyncS2CPacket;
import net.memeland.tutorialmod.networking.packet.FluidSyncS2CPacket;
import net.memeland.tutorialmod.networking.packet.ItemStackSyncS2CPacket;
import net.memeland.tutorialmod.recipe.DryingTrayRecipe;
import net.memeland.tutorialmod.screen.DryingTrayMenu;
import net.memeland.tutorialmod.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class DryingTrayBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) { // Reload the chunk / block pos if changes occur
            setChanged();
            if(!level.isClientSide) {
                ModMessages.sendToAllClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> stack.getItem() == ModItems.JUREMA_BARK.get() || stack.getItem() == Items.FERN; // May be better to create a TAG
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(60000, 256) {
        @Override
        public void onEnergyChange() {
            setChanged();
            //syncronize
            ModMessages.sendToAllClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };
    // energy per tick needed to craft something
    private static final int ENERGY_REQ = 32;

    private final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            //syncronize
            if(!level.isClientSide) {
                ModMessages.sendToAllClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER || stack.getFluid() == ModFluids.SOURCE_TROPICAL_WATER.get();
        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    public ItemStack getRenderStack() {
        ItemStack stack;

        if(!itemHandler.getStackInSlot(2).isEmpty()) {
            stack = itemHandler.getStackInSlot(2);
        } else {
            stack = itemHandler.getStackInSlot(1);
        }
        return stack;
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // Direction based item handling, crucial part of making machines that can work with hoppers, pipes, or other item transportation systems
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, // If the direction is down we can extract items from the result slot (2) OUTPUT like a hopper would do
                    LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),

                    Direction.NORTH, // Can insert into slot 1 (ingredient) only if item is valid for slot 1
                    LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 1, (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.SOUTH, // Like DOWN
                    LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),

                    Direction.EAST, /// Like NORTH
                    LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.WEST, // Can insert into slots 0 and 1 but only valid items for those slots
                    LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0 || index == 1, (index, stack) -> itemHandler.isItemValid(0, stack) || itemHandler.isItemValid(1, stack))));

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public DryingTrayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.DRYING_TRAY.get(), blockPos, blockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DryingTrayBlockEntity.this.progress;
                    case 1 -> DryingTrayBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DryingTrayBlockEntity.this.progress = value;
                    case 1 -> DryingTrayBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("tile.tutorialmod.drying_tray");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {

        // When the menu is opened/created we also syncronize the energy and fluid levels
        ModMessages.sendToAllClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        ModMessages.sendToAllClients(new FluidSyncS2CPacket(this.getFluidStack(), worldPosition));

        return new DryingTrayMenu(id, inventory, this, this.data);
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(DryingTrayBlock.FACING);

                if(side == Direction.UP || side == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                };
            }
        }
        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("drying_tray.progress", this.progress);
        nbt.putInt("drying_tray.energy", this.ENERGY_STORAGE.getEnergyStored());
        nbt = FLUID_TANK.writeToNBT(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("drying_tray.progress");
        ENERGY_STORAGE.setEnergy(nbt.getInt("drying_tray.energy"));
        FLUID_TANK.readFromNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, DryingTrayBlockEntity entity) { //This method gets called every 1/20 of a second
        if(level.isClientSide()) {
            return;
        }

        if(hasDriedInFirstSlot(entity)) {
            entity.ENERGY_STORAGE.receiveEnergy(64, false);
        }

        if(hasRecipe(entity) && hasEnoughEnergy(entity)) {
            entity.progress++;
            extractEnergy(entity);
            setChanged(level, blockPos, blockState);

            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            setChanged(level, blockPos, blockState);
        }

        if(hasFluidItemInSourceSlot(entity)) {
            transferItemFluidToFluidTank(entity);
        }
    }

    private static void transferItemFluidToFluidTank(DryingTrayBlockEntity entity) {
        entity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(entity.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if(entity.FLUID_TANK.isFluidValid(stack)) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(entity, stack, handler.getContainer()); // We need the container to change a bucket of fluid into an empty bucket
            }
        });
    }

    private static void fillTankWithFluid(DryingTrayBlockEntity entity, FluidStack stack, @NotNull ItemStack container) {
        entity.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        entity.itemHandler.extractItem(0, 1 , false);
        entity.itemHandler.insertItem(0, container, false);
    }

    private static boolean hasFluidItemInSourceSlot(DryingTrayBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(0).getCount() > 0;
    }

    private static void extractEnergy(DryingTrayBlockEntity entity) {
        entity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy(DryingTrayBlockEntity entity) {
        return entity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * entity.maxProgress;
    }

    private static boolean hasDriedInFirstSlot(DryingTrayBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(0).getItem() == ModItems.DRIED_JUREMA_BARK.get();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(DryingTrayBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        Optional<DryingTrayRecipe> recipe = level.getRecipeManager().getRecipeFor(DryingTrayRecipe.Type.INSTANCE, inventory, level);

        if(hasRecipe(entity)) {
            entity.FLUID_TANK.drain(recipe.get().getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            entity.itemHandler.extractItem(1, 1, false);
            entity.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
                    entity.itemHandler.getStackInSlot(2).getCount() + 1));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(DryingTrayBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<DryingTrayRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(DryingTrayRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem()) && hasCorrectFluidInTank(entity, recipe) && hasCorrectFluidAmout(entity, recipe);
    }

    private static boolean hasCorrectFluidAmout(DryingTrayBlockEntity entity, Optional<DryingTrayRecipe> recipe) {
        return entity.FLUID_TANK.getFluidAmount() >= recipe.get().getFluid().getAmount();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).isEmpty() || inventory.getItem(2).getItem() == stack.getItem();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }

    private  static boolean hasCorrectFluidInTank(DryingTrayBlockEntity entity, Optional<DryingTrayRecipe> recipe) {
        return recipe.get().getFluid().equals(entity.FLUID_TANK.getFluid());
    }
}
