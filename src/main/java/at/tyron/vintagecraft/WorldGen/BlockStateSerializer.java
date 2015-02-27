package at.tyron.vintagecraft.WorldGen;

import java.lang.reflect.Type;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BlockStateSerializer implements JsonSerializer<IBlockState>, JsonDeserializer<IBlockState> {

	@Override
	public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", ((ResourceLocation)Block.blockRegistry.getNameForObject(src.getBlock())).getResourcePath());
		obj.addProperty("meta", src.getBlock().getMetaFromState(src));
		return obj;
	}

	@Override
	public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Block.getBlockFromName(json.getAsJsonObject().get("name").getAsString()).getStateFromMeta(json.getAsJsonObject().get("meta").getAsInt());
	}

	
}
