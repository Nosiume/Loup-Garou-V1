package lg.roles.roles;

import lg.LGPlugin;
import lg.roles.Role;
import lg.roles.Type;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Hunter extends Role {

	public Hunter() {
		super("Chasseur", 1,
				false, true, false,
				Type.VILLAGER, 
				Sound.ENTITY_CREEPER_PRIMED,
				PotionEffectType.CONDUIT_POWER,
				"Vous gagnez avec le village. Lorsque vous vous faites tuer, vous pouvez décider de tuer quelqu'un d'autre.");
	
		this.item = new ItemStack(Material.ARROW, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§2Chasseur");
		item.setItemMeta(meta);
	}

	@Override
	public void onActivation(Player p) {
		if(canUse)
			p.sendMessage(LGPlugin.PREFIX + "§eVous pouvez selectionner une cible en faisant clic gauche");
	}

	@Override
	public void onStart(Player p) {
		p.sendMessage(LGPlugin.PREFIX + "§eVous êtes §c" + name + ".");
		p.sendMessage("§7 - " + getDesc());
	}

}
