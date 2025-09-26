package net.memeland.minecraftgov.data;

import net.minecraft.nbt.CompoundTag;

public class PlayerData {
    private String nationality;
    private int politicalSymbol; // 1-25
    private boolean hasReceivedIdCard; // Track if player has ever been given an ID card

    public PlayerData(String nationality, int politicalSymbol, boolean hasReceivedIdCard) {
        this.nationality = nationality;
        this.politicalSymbol = politicalSymbol;
        this.hasReceivedIdCard = hasReceivedIdCard;
    }

    public PlayerData() {
        this("", 1, false); // Default: no ID card given yet
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getPoliticalSymbol() {
        return politicalSymbol;
    }

    public void setPoliticalSymbol(int politicalSymbol) {
        this.politicalSymbol = politicalSymbol;
    }

    public boolean hasReceivedIdCard() {
        return hasReceivedIdCard;
    }

    public void setHasReceivedIdCard(boolean hasReceivedIdCard) {
        this.hasReceivedIdCard = hasReceivedIdCard;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("nationality", nationality);
        tag.putInt("politicalSymbol", politicalSymbol);
        tag.putBoolean("hasReceivedIdCard", hasReceivedIdCard);
        return tag;
    }

    public static PlayerData fromNBT(CompoundTag tag) {
        return new PlayerData(
                tag.getString("nationality"),
                tag.getInt("politicalSymbol"),
                tag.getBoolean("hasReceivedIdCard") // This will default to false for existing data
        );
    }
}