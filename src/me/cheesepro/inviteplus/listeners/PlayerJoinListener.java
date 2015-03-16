package me.cheesepro.inviteplus.listeners;

import me.cheesepro.inviteplus.InvitePlus;
import me.cheesepro.inviteplus.utils.Config;
import me.cheesepro.inviteplus.utils.Messenger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

/**
 * Created by Mark on 2015-03-14.
 */
public class PlayerJoinListener implements Listener{

    InvitePlus plugin;
    Map<String, List<String>> cache;
    Map<String, Integer> count;
    Map<String, Map<String, String>> rewards;
    Map<String, Integer> rewardHistories;
    Map<String, String> messages;
    Messenger msg;
    Config data;
    Boolean creditEnabled;

    public PlayerJoinListener(InvitePlus plugin){
        this.plugin = plugin;
        cache = plugin.getCache();
        count = plugin.getCount();
        msg = new Messenger(plugin);
        rewards = plugin.getRewards();
        data = plugin.getData();
        rewardHistories = plugin.getRewardHistories();
        creditEnabled = plugin.isCreditEnabled();
        messages = plugin.getMessages();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Player p = e.getPlayer();
                if (creditEnabled) {
                    msg.send(p, "e", "This plugin is coded by XxxCheeseproxxX A.K.A. Cheesepro A.K.A. Mark");
                    msg.send(p, "b", "Check out his spigot profile if you wish! http://www.spigotmc.org/members/xxxcheeseproxxx.23156/");
                    msg.send(p, "a", "Check out his website too! http://minefuturemc.com/");
                }
                if (!p.hasPlayedBefore()) {
                    String uuid = p.getUniqueId().toString();
                    outterloop:
                    for (String invitersCache : cache.keySet()) {
                        List<String> inviteds = cache.get(invitersCache);
                        for (String invitedCache : inviteds) {
                            if (invitedCache.equalsIgnoreCase(uuid)) {
                                OfflinePlayer inviter = Bukkit.getOfflinePlayer(UUID.fromString(invitersCache));
                                if (inviter != null) {
                                    String inviterName = inviter.getName();
                                    msg.send(p, "*", messages.get("first-time-join-invited-ask").replace("%target%", inviterName));
                                    msg.send(p, "*", messages.get("first-time-join-invited-command").replace("%target%", inviterName));
                                    break outterloop;
                                }
                            }
                        }
                    }
                } else {
                    for (String invitersCache : count.keySet()) {
                        if (p.getUniqueId().toString().equalsIgnoreCase(invitersCache)) {
                            for (String reward : rewards.keySet()) {
                                Map<String, String> value = rewards.get(reward);
                                if (rewardHistories.isEmpty()) {
                                    if (Integer.parseInt(value.get("count")) == count.get(invitersCache)) {
                                        if (rewardHistories.keySet().contains(p.getUniqueId().toString())) {
                                            data.set("rewardhistories." + p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                            data.saveConfig();
                                            rewardHistories.put(p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                        } else {
                                            data.set("rewardhistories." + p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                            data.saveConfig();
                                            rewardHistories.put(p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                        }
                                        if (value.get("message") != null) {
                                            msg.send(p, "*", value.get("message").replace("%player%", p.getName()));
                                        }
                                        if (value.get("broadcast") != null) {
                                            msg.broadcast(value.get("broadcast").replace("%player%", p.getName()));
                                        }
                                        if (value.get("command") != null) {
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), value.get("command").replaceFirst("/", "").replace("%player%", p.getName()));
                                        }
                                    }
                                } else {
                                    if (Integer.parseInt(value.get("count")) == count.get(invitersCache) && (count.get(invitersCache) > rewardHistories.get(invitersCache))) {
                                        if (rewardHistories.keySet().contains(p.getUniqueId().toString())) {
                                            data.set("rewardhistories." + p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                            data.saveConfig();
                                            rewardHistories.put(p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                        } else {
                                            data.set("rewardhistories." + p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                            data.saveConfig();
                                            rewardHistories.put(p.getUniqueId().toString(), count.get(p.getUniqueId().toString()));
                                        }
                                        if (value.get("message") != null) {
                                            msg.send(p, "*", value.get("message").replace("%player%", p.getName()));
                                        }
                                        if (value.get("broadcast") != null) {
                                            msg.broadcast(value.get("broadcast").replace("%player%", p.getName()));
                                        }
                                        if (value.get("command") != null) {
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), value.get("command").replaceFirst("/", "").replace("%player%", p.getName()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 60);
    }

}
