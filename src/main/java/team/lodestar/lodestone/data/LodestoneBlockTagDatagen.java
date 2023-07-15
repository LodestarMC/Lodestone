package team.lodestar.lodestone.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class LodestoneBlockTagDatagen extends BlockTagsProvider {

	public LodestoneBlockTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, modId, existingFileHelper);
	}

	@Override
	@NotNull
	public String getName() {
		return "Lodestone Block Tags";
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {

	}
}