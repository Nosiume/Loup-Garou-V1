package lg.roles.roles;

import java.util.ArrayList;
import java.util.List;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.RoleManager;
import lg.roles.Roles;
import lg.roles.Type;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Detective extends Role {

	public List<Player> suspects = new ArrayList<Player>();
	
	public Detective() {
		super("Détective", 1,
				true, false, false,
				Type.VILLAGER,
				Sound.BLOCK_ENCHANTMENT_TABLE_USE,
				PotionEffectType.WATER_BREATHING,
				"Chaque nuit, vous devez désigner deux personnes, ainsi, si les deux personnes ne sont pas dans le même camp, vous le saurez. ⚠ Vous ne pouvez pas séléctionner deux fois la même personne.",
				2);
	
		this.item = new ItemStack(Material.TRIPWIRE_HOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eDetective");
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		suspects.clear();
		canUse = true;
		
		String json = "[{" + LGPlugin.JSON_PREFIX + "\"text\":\"Veuillez choisir 2 personnes à comparer. \",\"color\":\"yellow\"},{\"text\":\"[CLIQUE ICI]\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/lg-detective\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Sélection des joueurs\"}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + "§e.");
		p.sendMessage("§7 - " + getDesc());
	}

	public void onSelect(Player p) {
		RoleManager rm = LGPlugin.getRoleManager();
		
		//We suppose that suspects list contains 2 players.
		List<Role> suspect1 = rm.getPlayerRole(suspects.get(0));		
		List<Role> suspect2 = rm.getPlayerRole(suspects.get(1));
		
		Type type1 = null;
		Type type2 = null;
		
		if(suspect1.size() > 1)
			type1 = Type.WEREWOLF;
		
		if(suspect2.size() > 1)
			type2 = Type.WEREWOLF;
		
		if(type1 == null && 
				LGPlugin.getRoleManager().getRoleID(suspect1.get(0)) == Roles.WEREWOLF)
		{
			Werewolf werewolf = (Werewolf) suspect1.get(0);
			if(werewolf.isAnonyme(suspects.get(0)))
				type1 = Type.VILLAGER;
			else
				type1 = Type.WEREWOLF;
		}
		
		if(type2 == null && 
				LGPlugin.getRoleManager().getRoleID(suspect2.get(0)) == Roles.WEREWOLF)
		{
			Werewolf werewolf = (Werewolf) suspect2.get(0);
			if(werewolf.isAnonyme(suspects.get(1)))
				type2 = Type.VILLAGER;
			else
				type2 = Type.WEREWOLF;
		}
		
		boolean isSameType = type1 == type2;
		
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
		p.sendMessage("§7------------------------------------");
		p.sendMessage("");
		p.sendMessage("    §eLes deux suspects ont des types de rôles");
		p.sendMessage("                  §c" + (isSameType ? "Egaux" : "Différents"));
		p.sendMessage("");
		p.sendMessage("§7------------------------------------");
	}

}
