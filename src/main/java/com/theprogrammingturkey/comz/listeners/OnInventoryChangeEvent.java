package com.theprogrammingturkey.comz.listeners;

import com.theprogrammingturkey.comz.game.Game;
import com.theprogrammingturkey.comz.game.Game.ArenaStatus;
import com.theprogrammingturkey.comz.game.GameManager;
import com.theprogrammingturkey.comz.guns.Gun;
import com.theprogrammingturkey.comz.guns.GunManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryChangeEvent implements Listener
{

	@EventHandler
	public void onInventoryChangeEvent(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if(GameManager.INSTANCE.isPlayerInGame(player))
		{
			event.setCancelled(true);
		}
		Game game = GameManager.INSTANCE.getGame(player);
		if(game == null || !(game.mode == ArenaStatus.INGAME))
			return;
		if(game.getPlayersGun(player) != null)
		{
			GunManager gunManager = game.getPlayersGun(player);
			if(gunManager.isGun())
			{
				Gun gun = gunManager.getGun(player.getInventory().getHeldItemSlot());
				gun.reload();
				gun.updateGun();
			}
		}
	}
}
