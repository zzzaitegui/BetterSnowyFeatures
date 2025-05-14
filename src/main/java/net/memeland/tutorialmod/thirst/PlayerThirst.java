package net.memeland.tutorialmod.thirst;

import net.minecraft.nbt.CompoundTag;

public class PlayerThirst {
    private int thirst;
    private final int MAX_THIRST = 10;
    private final int MIN_THIRST = 0;

    public int getThirst() {
        return thirst;
    }

    public void addThirst(int add) {
        this.thirst = Math.min(thirst + add, MAX_THIRST); // If when adding we go over the max then thirst + add WONT be Math.min
    }

    public void subThirst(int sub) {
        this.thirst = Math.max(thirst - sub, MIN_THIRST); // If when adding we go over the max then thirst + add WONT be Math.min
    }

    public void copyFrom(PlayerThirst source) {
        this.thirst = source.thirst;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("thirst", thirst);
    }

    public void loadNBTData(CompoundTag nbt) {
        thirst = nbt.getInt("thirst");
    }
}
