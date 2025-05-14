package net.memeland.tutorialmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.memeland.tutorialmod.TutorialMod;
import net.memeland.tutorialmod.util.FluidJSONUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class DryingTrayRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final FluidStack fluidStack;

    public DryingTrayRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, FluidStack fluidStack) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.fluidStack = fluidStack;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if(level.isClientSide()) {
            return false;
        }
        return recipeItems.get(0).test(simpleContainer.getItem(1));
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<DryingTrayRecipe> {
        public Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "drying";
    }

    public static class Serializer implements RecipeSerializer<DryingTrayRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TutorialMod.MOD_ID, "drying");

        @Override
        public DryingTrayRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            FluidStack fluid = FluidJSONUtil.readFluid(jsonObject.get("fluid").getAsJsonObject());

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            return new DryingTrayRecipe(resourceLocation, output, inputs, fluid);
        }

        @Override
        public @Nullable DryingTrayRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            FluidStack fluid = buf.readFluidStack();

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new DryingTrayRecipe(resourceLocation, output, inputs, fluid);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DryingTrayRecipe dryingTrayRecipe) {
            buf.writeInt(dryingTrayRecipe.getIngredients().size());
            buf.writeFluidStack(dryingTrayRecipe.fluidStack);

            for (Ingredient ing : dryingTrayRecipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(dryingTrayRecipe.getResultItem(), false);
        }
    }


}
