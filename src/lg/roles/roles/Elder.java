package lg.roles.roles;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.Type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Elder extends Role {
	
	public Elder() {
		super("Ancien", 1,
				false, true, false,
				Type.VILLAGER, 
				null,
				PotionEffectType.DAMAGE_RESISTANCE,
				"Vous ne vous avourez pas vaincu ! Si vous vous faites tuer par les loups garou pendant la nuit, vous ressusciterez (une seule fois hein).");
	
		this.item = new ItemStack(Material.SKELETON_SKULL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cAncien");
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		if(canUse == true)
			p.sendMessage(LGPlugin.PREFIX + "§aOups... Les loups garous ont essayés de vous tuer. Vous avez été ressuscité");
		else
			p.sendMessage(LGPlugin.PREFIX + "§aBon, on ne peut pas survivre à tous les coups.");
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + ".");
		p.sendMessage("§7 - " + getDesc());
	}

}
