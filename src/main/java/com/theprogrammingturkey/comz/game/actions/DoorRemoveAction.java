package com.theprogrammingturkey.comz.game.actions;

import com.theprogrammingturkey.comz.COMZombies;
import com.theprogrammingturkey.comz.commands.CommandUtil;
import com.theprogrammingturkey.comz.game.Game;
import com.theprogrammingturkey.comz.game.features.Door;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class DoorRemoveAction extends BaseAction
{
	public DoorRemoveAction(Player player, Game game)
	{
		super(player, game);

		for(Door door : game.doorManager.getDoors())
		{
			for(Sign sign : door.getSigns())
			{
				if(!(sign.getBlock().getState() instanceof Sign))
				{
					sign.getBlock().setType(Material.OAK_WALL_SIGN);
				}
				sign.setLine(0, ChatColor.RED + "Break a sign");
				sign.setLine(1, ChatColor.RED + "to remove the");
				sign.setLine(2, ChatColor.RED + "door that the");
				sign.setLine(3, ChatColor.RED + "sign is for!");
				sign.update();
				sign.update(true);
			}
		}
		CommandUtil.sendMessageToPlayer(player, ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.DARK_RED + "Door Removal" + ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "---------------");
		CommandUtil.sendMessageToPlayer(player, ChatColor.GOLD + "Break any sign that leads to a door to remove the door!");
		CommandUtil.sendMessageToPlayer(player, ChatColor.RED + "Type cancel to cancel this operation.");
	}

	public void cancelAction()
	{
		for(Door door : game.doorManager.getDoors())
		{
			if(door.isOpened())
				door.closeDoor();
			for(Sign sign : door.getSigns())
			{
				sign.setLine(0, ChatColor.RED + "[Zombies]");
				sign.setLine(1, ChatColor.GOLD + "Door");
				sign.setLine(2, ChatColor.GOLD + "Price:");
				sign.setLine(3, Integer.toString(door.getCost()));
				sign.update();
			}
		}
		CommandUtil.sendMessageToPlayer(player, ChatColor.RED + "Door removal operation has been canceled!");
	}

	@Override
	public void onBlockBreakevent(BlockBreakEvent event)
	{
		Location loc = event.getBlock().getLocation();
		Door door = game.doorManager.getDoorFromSign(loc);
		if(door == null)
			return;

		door.removeSelfFromConfig();
		event.setCancelled(true);
		for(final Sign sign : door.getSigns())
			sign.getLocation().getBlock().setType(Material.AIR);

		CommandUtil.sendMessageToPlayer(player, ChatColor.GREEN + "" + ChatColor.BOLD + "Door removed!");
		game.doorManager.removeDoor(door);
		if(game.doorManager.getDoors().size() == 0)
		{
			CommandUtil.sendMessageToPlayer(player, ChatColor.RED + "No doors left!");
			cancelAction();
			COMZombies.getPlugin().activeActions.remove(player);
		}
	}
}