package fuzs.deathcompass.world.item;

import fuzs.deathcompass.registry.ModRegistry;
import fuzs.deathcompass.world.entity.player.PlayerDeathTracker;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DeathCompassItem extends Item implements IVanishable {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

   public DeathCompassItem(Properties p_40718_) {
      super(p_40718_);
   }

   @Override
   public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (itemstack.hasTag()) {
         final ITextComponent component = this.getDistanceComponent(player, itemstack.getTag());
         player.displayClientMessage(component, true);
         return ActionResult.sidedSuccess(itemstack, level.isClientSide());
      }
      return super.use(level, player, hand);
   }

   private ITextComponent getDistanceComponent(PlayerEntity source, CompoundNBT tag) {
      final Optional<RegistryKey<World>> lastDeathDimension = getLastDeathDimension(tag);
      if (lastDeathDimension.isPresent() && lastDeathDimension.get() != source.level.dimension()) {
         return new TranslationTextComponent("death.message.distance.dimension");
      } else {
         BlockPos blockpos = NBTUtil.readBlockPos(tag.getCompound("LastDeathPos"));
         double distance = source.position().distanceTo(new Vector3d(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
         if (distance < 3.0) {
            return new TranslationTextComponent("death.message.distance.close");
         } else {
            return new TranslationTextComponent("death.message.distance.blocks", (int) distance);
         }
      }
   }

   public static Optional<RegistryKey<World>> getLastDeathDimension(CompoundNBT p_40728_) {
      return World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, p_40728_.get("LastDeathDimension")).result();
   }

   public static Optional<ItemStack> createDeathCompass(PlayerEntity player) {
      PlayerDeathTracker tracker = (PlayerDeathTracker) player;
      if (tracker.hasLastDeathData()) {
         ItemStack itemstack = new ItemStack(ModRegistry.DEATH_COMPASS_ITEM.get(), 1);
         itemstack.setHoverName(new TranslationTextComponent("item.deathcompass.death_compass").append(new TranslationTextComponent("item.deathcompass.death_compass.player", player.getDisplayName())));
         CompoundNBT compoundtag = itemstack.hasTag() ? itemstack.getTag() : new CompoundNBT();
         itemstack.setTag(compoundtag);
         addLastDeathTags(tracker.getLastDeathDimension(), tracker.getLastDeathPosition(), tracker.getLastDeathDate(), compoundtag);
         return Optional.of(itemstack);
      }
      return Optional.empty();
   }

   private static void addLastDeathTags(RegistryKey<World> lastDeathDimension, BlockPos lastDeathPosition, long lastDeathDate, CompoundNBT p_40735_) {
      p_40735_.put("LastDeathPos", NBTUtil.writeBlockPos(lastDeathPosition));
      World.RESOURCE_KEY_CODEC.encodeStart(NBTDynamicOps.INSTANCE, lastDeathDimension).resultOrPartial(LOGGER::error).ifPresent((p_40731_) -> {
         p_40735_.put("LastDeathDimension", p_40731_);
      });
      p_40735_.putLong("LastDeathDate", lastDeathDate);
   }

   @Override
   public void appendHoverText(ItemStack p_4itemStack457_, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
      if (p_4itemStack457_.hasTag()) {
         CompoundNBT tag = p_4itemStack457_.getTag();
         BlockPos blockpos = NBTUtil.readBlockPos(tag.getCompound("LastDeathPos"));
         tooltip.add(new TranslationTextComponent("item.deathcompass.death_compass.tooltip.position", new StringTextComponent(String.valueOf(blockpos.getX())).withStyle(TextFormatting.GRAY), new StringTextComponent(String.valueOf(blockpos.getY())).withStyle(TextFormatting.GRAY), new StringTextComponent(String.valueOf(blockpos.getZ())).withStyle(TextFormatting.GRAY)).withStyle(TextFormatting.GOLD));
         final Optional<RegistryKey<World>> lastDeathDimension = getLastDeathDimension(tag);
         if (lastDeathDimension.isPresent()) {
            tooltip.add(new TranslationTextComponent("item.deathcompass.death_compass.tooltip.dimension", new StringTextComponent(lastDeathDimension.get().location().toString()).withStyle(TextFormatting.GRAY)).withStyle(TextFormatting.GOLD));
         }
         final long lastDeathDate = tag.getLong("LastDeathDate");
         if (lastDeathDate > 0) {
            String date = DATE_FORMAT.format(new Date(lastDeathDate));
            tooltip.add(new TranslationTextComponent("item.deathcompass.death_compass.tooltip.date", new StringTextComponent(date).withStyle(TextFormatting.GRAY)).withStyle(TextFormatting.GOLD));
         }
      }
   }
}