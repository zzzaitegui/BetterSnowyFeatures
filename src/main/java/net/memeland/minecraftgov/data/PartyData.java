package net.memeland.minecraftgov.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

public class PartyData {
    private String name;
    private int symbolId; // 1-25
    private DyeColor color;
    private String representative;

    public PartyData(String name, int symbolId, DyeColor color, String representative) {
        this.name = name;
        this.symbolId = symbolId;
        this.color = color;
        this.representative = representative;
    }

    public PartyData() {
        this("", 1, DyeColor.WHITE, "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putInt("symbolId", symbolId);
        tag.putInt("color", color.getId());
        tag.putString("representative", representative);
        return tag;
    }

    public static PartyData fromNBT(CompoundTag tag) {
        return new PartyData(
                tag.getString("name"),
                tag.getInt("symbolId"),
                DyeColor.byId(tag.getInt("color")),
                tag.getString("representative")
        );
    }
}