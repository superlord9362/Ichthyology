package fuffles.ichthyology.common.world.level.storage.loot.functions;

import com.google.gson.*;
import fuffles.ichthyology.init.ModLootItemFunctions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransferTagValueFunction extends LootItemConditionalFunction
{
    private final String[] sourcePath;
    private final String[] destinationPath;

    public TransferTagValueFunction(String[] sourcePath, String[] destinationPath, LootItemCondition[] conditions)
    {
        super(conditions);
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    @NotNull
    @Override
    protected ItemStack run(@NotNull ItemStack stack, @NotNull LootContext context)
    {
        Entity victim = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (victim != null)
        {
            Tag value = null;
            CompoundTag tag = victim.saveWithoutId(new CompoundTag());
            int j = this.sourcePath.length - 1;
            for (int i = 0; i < this.sourcePath.length; i++)
            {
                if (i < j)
                    tag = tag.getCompound(this.sourcePath[i]);
                else
                    value = tag.get(this.sourcePath[i]);
            }
            if (value != null)
            {
                tag = stack.getOrCreateTag();
                j = this.destinationPath.length - 1;
                for (int i = 0; i < this.destinationPath.length; i++)
                {
                    if (i < j)
                    {
                        if (!tag.contains(this.destinationPath[i], Tag.TAG_COMPOUND))
                            tag.put(this.destinationPath[i], new CompoundTag());
                        tag = tag.getCompound(this.destinationPath[i]);
                    }
                    else
                    {
                        tag.put(this.destinationPath[i], value);
                    }
                }
            }
        }
        return stack;
    }

    @NotNull
    @Override
    public LootItemFunctionType getType()
    {
        return ModLootItemFunctions.TRANSFER_TAG_VALUE;
    }

    public static LootItemConditionalFunction.Builder<?> transferred(String[] path)
    {
        return transferred(path, path);
    }

    public static LootItemConditionalFunction.Builder<?> transferred(String[] source, String[] destination)
    {
        return simpleBuilder(lootItemConditions -> new TransferTagValueFunction(source, destination, lootItemConditions));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<TransferTagValueFunction>
    {
        @Override
        public void serialize(@NotNull JsonObject json, @NotNull TransferTagValueFunction value, @NotNull JsonSerializationContext context)
        {
            super.serialize(json, value, context);
            JsonArray source = new JsonArray();
            for (String path : value.sourcePath)
            {
                source.add(path);
            }
            json.add("source", source);
            if (!Objects.equals(value.sourcePath, value.destinationPath))
            {
                JsonArray destination = new JsonArray();
                for (String path : value.destinationPath) {
                    destination.add(path);
                }
                json.add("destination", destination);
            }
        }

        @NotNull
        @Override
        public TransferTagValueFunction deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context, LootItemCondition @NotNull [] conditions)
        {
            List<String> sourcePath = new ArrayList<>();
            JsonArray source = GsonHelper.getAsJsonArray(json, "source");
            if (source.isEmpty())
                throw new JsonSyntaxException("source JsonArray must have elements");
            for (JsonElement element : source)
            {
                if (!GsonHelper.isStringValue(element))
                    throw new JsonSyntaxException("source JsonArray does not contain string values!");
                sourcePath.add(element.getAsJsonPrimitive().getAsString());
            }
            if (json.has("destination") && json.get("destination").isJsonArray() && !json.getAsJsonArray("destination").isEmpty())
            {
                List<String> destinationPath = new ArrayList<>();
                JsonArray destination = GsonHelper.getAsJsonArray(json, "destination");
                for (JsonElement element : destination)
                {
                    if (!GsonHelper.isStringValue(element))
                        throw new JsonSyntaxException("destination JsonArray does not contain string values!");
                    destinationPath.add(element.getAsJsonPrimitive().getAsString());
                }
                return new TransferTagValueFunction(sourcePath.toArray(new String[0]), destinationPath.toArray(new String[0]), conditions);
            }
            else
            {
                String[] array = sourcePath.toArray(new String[0]);
                return new TransferTagValueFunction(array, array, conditions);
            }
        }
    }
}