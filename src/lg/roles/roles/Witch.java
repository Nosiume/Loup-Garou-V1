package lg.roles.roles;

import lg.LGPlugin;
import lg.game.LGGame;
import lg.roles.Role;
import lg.roles.Type;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Witch extends Role {

	public static ItemStack savePot;
	public static ItemStack killPot;
	
	private boolean canUseSave = true;
	private boolean canUseKill = true;
	
	public Witch() {
		super("Sorcière", 1, 
				true, false, false, 
				Type.VILLAGER, 
				Sound.ENTITY_WITCH_AMBIENT,
				PotionEffectType.FAST_DIGGING,
				"Vous possèdez deux potions, une potion de vie et une potion de mort, chaque nuit, vous pouvez décider de ressusciter un villageois qui a été tué par les loups garous, ou de tuer un villageois que vous trouvez suspect. Vous ne pouvez utiliser qu'une potion par nuit, et vous n'en avez qu'une de chaque.");
	
		this.item = new ItemStack(Material.CAULDRON, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSorcière");
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"C'est à votre tour d'agir. Vous pouvez utiliser une potion. \",\"color\":\"yellow\"},{\"text\":\"[UTILISER UNE POTION]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-witch\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Stock de Potion.\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		savePot = new ItemStack(Material.SPLASH_POTION, 1);
		PotionMeta sMeta = (PotionMeta) savePot.getItemMeta();
		sMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 255), true);
		sMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		sMeta.setDisplayName("§5Sauver quelqu'un");
		savePot.setItemMeta(sMeta);
		
		killPot = new ItemStack(Material.POTION, 1);
		PotionMeta kMeta = (PotionMeta) killPot.getItemMeta();
		kMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 1, 255), true);
		kMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		kMeta.setDisplayName("§7Tuer quelqu'un");
		killPot.setItemMeta(kMeta);
		
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §cSorcière.");
		p.sendMessage("§7 - " + getDesc());
	}
	
	public void save(Player witch, Player target)
	{
		LGGame.toKill.remove(target);
		witch.sendMessage(LGPlugin.PREFIX
				+ "§aLe joueur a été sauvé !");
		canUseSave = false;
	}
	
	public void kill(Player witch, Player target)
	{
		LGGame.toKill.add(target);
		witch.sendMessage(LGPlugin.PREFIX
				+ "§aLa victime mourra au levé du jour.");
		canUseKill = false;
	}
	
	public boolean canSave()
	{
		return canUseSave;
	}
	
	public boolean canKill()
	{
		return canUseKill;
	}

}
