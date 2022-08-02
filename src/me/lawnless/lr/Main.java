package me.lawnless.lr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
	
	private ArrayList<String> randomMaterials = new ArrayList<String>();
	public static List<String> allPlayers = new ArrayList<>();
	
	@Override
	public void onEnable() {
		for (Material material : Material.values()) {
			if (material.isItem() || material.isBlock()) {
				if (!material.name().equalsIgnoreCase("COMMAND_BLOCK") && !material.name().equalsIgnoreCase("CHAIN_COMMAND_BLOCK") && !material.name().equalsIgnoreCase("COMMAND_BLOCK_MINECART") && !material.name().equalsIgnoreCase("AIR") && !material.name().equalsIgnoreCase("FIRE")) {
					randomMaterials.add(material.name());
				}
			}
		}
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("LawnsRandom plugini aktif edildi.");
		/* Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    @Override
		    public void run() {
		    	for (Player p : Bukkit.getOnlinePlayers()) {
		    		Block block = p.getLocation().getBlock();
		    		p.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
		    		// block.setType(Material.TNT);
		    	}
		    }
		}, 0L, 30*20L); */
	}
	
	@Override
	public void onDisable() {
		getLogger().info("LawnsRandom plugini de-aktif edildi.");
	}
	
	/* @EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Location loc = getRandomLocation();
		p.teleport(loc);
	} */
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage("§e"+p.getName()+" oyuna katıldı");
		allPlayers.add(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage("§e"+p.getName()+" oyundan ayrıldı");
		allPlayers.remove(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Random rand = new Random();
		int n = rand.nextInt(100) + 1;
		if (n <= 25) {
			Player p = e.getPlayer();
			int rnd3 = ThreadLocalRandom.current().nextInt(PotionEffectType.values().length);
			p.addPotionEffect(new PotionEffect(PotionEffectType.values()[rnd3], 200, 1));
		} else {
			Player p = e.getPlayer();
			Block b = e.getBlock();
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			int rand2 = getRandomInt(randomMaterials.size());
			Material m = Material.getMaterial(randomMaterials.get(rand2));
			p.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(m, 1));
		}
	}
	
	public Location getRandomLocation() {
	    World world = Bukkit.getWorld("world");
	    Random rand = new Random();
	    
	    int rangeMax = 2500;
	    int rangeMin = -2500;

	    int X = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int Z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int Y = world.getHighestBlockYAt(X, Z);

	    return new Location(world, X, Y, Z).add(0.5, 0, 0.5);
	}
	
	public static Integer getRandomInt(Integer max) {
		Random ran = new Random();
		return ran.nextInt(max);
	}
	
	public static int randomPlayer(Integer i) {
        int random = ThreadLocalRandom.current().nextInt(i);
        return random;
    }
	
    public static String getRandomPlayer() {
        String s = allPlayers.get(randomPlayer(allPlayers.size()));
        return s;
    }

}
