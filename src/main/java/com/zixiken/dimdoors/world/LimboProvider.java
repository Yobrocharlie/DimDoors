package com.zixiken.dimdoors.world;

import com.zixiken.dimdoors.DimDoors;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import com.zixiken.dimdoors.CloudRenderBlank;
import com.zixiken.dimdoors.config.DDProperties;
import com.zixiken.dimdoors.ticking.CustomLimboPopulator;
import com.zixiken.dimdoors.util.Point4D;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LimboProvider extends WorldProvider
{
	@Override
	public String getDimensionName() {
		return "Limbo";
	}

	private IRenderHandler skyRenderer;
	private DDProperties properties;
	private CustomLimboPopulator spawner;

	public LimboProvider() {
		this.hasNoSky = false;
		this.skyRenderer = new LimboSkyProvider();
		this.spawner = DimDoors.spawner;
		this.properties = DimDoors.properties;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		return this.skyRenderer;
	}

	@Override
	protected void registerWorldChunkManager() {
		super.worldChunkMgr = new WorldChunkManagerHell(DimDoors.limboBiome,1);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
		return DimDoors.limboBiome;
	}

	@Override
	public boolean canRespawnHere() {
		return properties.HardcoreLimboEnabled;
	}

	@Override
	public boolean isBlockHighHumidity(BlockPos pos) {
		return false;
	}


	@Override
	public boolean canSnowAt(BlockPos pos, boolean checkLight)
	{
		return false;
	}
	
	@Override
	protected void generateLightBrightnessTable() {
		float modifier = 0.0F;

		for (int steps = 0; steps <= 15; ++steps) {
			float var3 = 1.0F - steps / 15.0F;
			this.lightBrightnessTable[steps] = ((0.0F + var3) / (var3 * 3.0F + 1.0F) * (1.0F - modifier) + modifier)*3;
			//     System.out.println( this.lightBrightnessTable[steps]+"light");
		}
	}

	@Override
	public BlockPos getSpawnPoint() {
		return this.getRandomizedSpawnPoint();
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		int var4 = (int)(par1 % 24000L);
		float var5 = (var4 + par3) / 24000.0F - 0.25F;

		if (var5 < 0.0F) {
			++var5;
		}

		if (var5 > 1.0F) {
			--var5;
		}

		float var6 = var5;
		var5 = 1.0F - (float)((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public int getMoonPhase(long par1) {
		return 4;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getSaveFolder() {
		return (dimensionId == 0 ? null : "DimensionalDoors/Limbo" + dimensionId);
	}

	@Override
	public boolean canCoordinateBeSpawn(int par1, int par2) {
		Block block = this.worldObj.getBlockState(this.worldObj.getTopSolidOrLiquidBlock(new BlockPos(par1, 0, par2))).getBlock();
		return block == DimDoors.blockLimbo;
	}
	@Override
	public double getHorizon() {
		return worldObj.getHeight()/4-800;
	}

	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		setCloudRenderer( new CloudRenderBlank());
		return new Vec3(0, 0, 0);

	}
	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getFogColor(float par1, float par2) {
		return new Vec3(.2, .2, .2);

	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		return 0;
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new LimboGenerator(worldObj, 45, spawner, properties);
	}
	
	@Override
	public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
		return false;
	}

	public static Point4D getLimboSkySpawn(EntityPlayer player, DDProperties properties) {
		int x = (int) (player.posX) + MathHelper.getRandomIntegerInRange(player.worldObj.rand, -properties.LimboEntryRange, properties.LimboEntryRange);
		int z = (int) (player.posZ) + MathHelper.getRandomIntegerInRange(player.worldObj.rand, -properties.LimboEntryRange, properties.LimboEntryRange);
		return new Point4D(new BlockPos(x, 700, z), properties.LimboDimensionID);
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint() {
		int x = MathHelper.getRandomIntegerInRange(this.worldObj.rand, -500, 500);
		int z = MathHelper.getRandomIntegerInRange(this.worldObj.rand, -500, 500);
		return new BlockPos(x, 700, z);
	}

	@Override
	public String getInternalNameSuffix() {
		return "_limbo";
	}

}