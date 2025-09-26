package net.memeland.minecraftgov.screen;

import net.memeland.minecraftgov.ModgovMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModgovMod.MOD_ID);

    public static final RegistryObject<MenuType<BallotBoxMenu>> BALLOT_BOX_MENU =
            registerMenuType(BallotBoxMenu::new, "ballot_box_menu");

    public static final RegistryObject<MenuType<BulletinBoardMenu>> BULLETIN_BOARD_MENU =
            registerMenuType(BulletinBoardMenu::new, "bulletin_board_menu");

    public static final RegistryObject<MenuType<IdCardMenu>> ID_CARD_MENU =
            registerMenuType(IdCardMenu::new, "id_card_menu");

    public static final RegistryObject<MenuType<PamphletMenu>> PAMPHLET_MENU =
            registerMenuType(PamphletMenu::new, "pamphlet_menu");

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
