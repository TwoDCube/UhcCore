package com.gmail.val59000mc.scenarios.scenariolisteners;

import com.gmail.val59000mc.events.UhcTimeEvent;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.Option;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class WeakestLinkListener extends ScenarioListener {

    @Option
    private final long delay = 10 * 60;

    @EventHandler
    public void onUhcTime(UhcTimeEvent e) {
        // Run every minute
        if (e.getTotalTime() % delay != 0) {
            return;
        }

        UhcPlayer lowest = getLowestHealthPlayer();

        // Two or more players share the lowest health.
        if (lowest == null) {
            return;
        }

        // Kill player
        try {
            Player player = lowest.getPlayer();
            player.damage(player.getHealth());
        } catch (UhcPlayerNotOnlineException ex) {
            ex.printStackTrace();
        }

        getGameManager().broadcastMessage(Lang.SCENARIO_WEAKESTLINK_KILL.replace("%player%", lowest.getName()));
    }

    private UhcPlayer getLowestHealthPlayer() {
        UhcPlayer lowestPlayer = null;
        double lowestHealth = 0;

        // Look for player with lowest health.
        for (UhcPlayer uhcPlayer : getPlayersManager().getOnlinePlayingPlayers()) {
            try {
                if (lowestPlayer == null) {
                    lowestPlayer = uhcPlayer;
                    lowestHealth = uhcPlayer.getPlayer().getHealth();
                } else {
                    double playerHealth = uhcPlayer.getPlayer().getHealth();
                    if (playerHealth < lowestHealth) {
                        lowestPlayer = uhcPlayer;
                        lowestHealth = playerHealth;
                    }
                }
            } catch (UhcPlayerNotOnlineException ex) {
                ex.printStackTrace();
            }
        }

        // Check for player with same health.
        for (UhcPlayer uhcPlayer : getPlayersManager().getOnlinePlayingPlayers()) {
            // Don't check itself
            if (lowestPlayer == uhcPlayer) {
                continue;
            }

            // Check for player with same health, if it exists return null.
            try {
                if (lowestHealth == uhcPlayer.getPlayer().getHealth()) {
                    return null;
                }
            } catch (UhcPlayerNotOnlineException ex) {
                ex.printStackTrace();
            }
        }

        return lowestPlayer;
    }

}