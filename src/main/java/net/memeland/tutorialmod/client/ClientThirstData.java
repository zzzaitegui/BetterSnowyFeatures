package net.memeland.tutorialmod.client;

// We can do this with static integers because this will always only be on the client so every client will have a unique thirst, no need for instances
public class ClientThirstData {
    private static int playerThirst;

    public static void set(int thirst) {
        ClientThirstData.playerThirst = thirst;
    }

    public static int getPlayerThirst() {
        return playerThirst;
    }
}
