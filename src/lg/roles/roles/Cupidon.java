package lg.roles.roles;

import java.util.ArrayList;
import java.util.List;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.Type;
import lg.utils.Utils;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Cupidon extends Role {

	public List<Player> couple = new ArrayList<Player>();
	private List<ArmorStand> displays = new ArrayList<ArmorStand>();
	
	public Cupidon() {
		super("Cupidon", 1,
				false, false, true,
				Type.VILLAGER, 
				Sound.ENTITY_ARROW_SHOOT,
				PotionEffectType.WEAKNESS,
				"Vous devez désigner deux personnes qui tomberont amoureux. Vous devez gagner avec eux.");
	
		this.item = new ItemStack(Material.BOW, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§dCupidon");
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Veuillez sélectionner 2 amoureux. \",\"color\":\"yellow\"},{\"text\":\"[CLIQUE ICI]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-cupidon\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Selection du Couple.\"}}]"));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + ".");
		p.sendMessage("§7 - " + getDesc());
	}

	public void onChoose() {
		
		//Couple displays
		for(Player p : couple)
		{
			p.sendMessage(LGPlugin.PREFIX + "§5Vous êtes en couple avec §c" + (couple.indexOf(p) == 0 ? couple.get(1).getName() : couple.get(0).getName()));
			p.sendMessage(LGPlugin.PREFIX + "§7Vous devez gagner avec le cupidon.");
			p.sendMessage(LGPlugin.PREFIX + "§7Si un membre du couple meurs, l'autre se suicidera de tristesse.");
			p.sendMessage(LGPlugin.PREFIX + "§eLe cupidon est §c" + players.get(0).getName());
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 1f);
		
			ArmorStand armor = (ArmorStand) Utils.create(EntityType.ARMOR_STAND, p.getLocation().add(0, 2.6, 0));
			armor.setInvulnerable(true);
			armor.setVisible(false);
			armor.setGravity(false);
			armor.setCustomNameVisible(true);
			armor.setBasePlate(false);
			armor.setMarker(true);
			armor.setCustomName("§d❤ Couple ❤");
			
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving) ((CraftEntity) armor).getHandle());
			
			couple.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
			
			players.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
			
			displays.add(armor);
		}
		
		//Cupidon display
		for(Player p : players)
		{
			ArmorStand armor = (ArmorStand) Utils.create(EntityType.ARMOR_STAND, p.getLocation().add(0, 2, 0));
			armor.setInvulnerable(true);
			armor.setVisible(false);
			armor.setGravity(false);
			armor.setCustomNameVisible(true);
			armor.setBasePlate(false);
			armor.setMarker(true);
			armor.setCustomName("§5❤ Cupidon ❤");
			
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving) ((CraftEntity) armor).getHandle());
			
			couple.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
			
			players.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			});
			
			displays.add(armor);
		}
	}
	
	//Will be called at endgame
	public void clear()
	{
		for(ArmorStand armor : displays)
		{
			PacketPlayOutEntityDestroy packetR = new PacketPlayOutEntityDestroy(((CraftEntity) armor).getEntityId());
			
			couple.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetR);
			});
			
			players.forEach(player -> {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetR);
			});
		}
		
		displays.clear();
	}

}
