package net.cubespace.geSuitHomes.managers;

import net.cubespace.geSuitHomes.geSuitHomes;
import net.cubespace.geSuitHomes.tasks.PluginMessageTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class HomesManager {

    public static void deleteHome( CommandSender sender, String home ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "DeleteHome" );
            out.writeUTF( sender.getName() );
            out.writeUTF( home );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( geSuitHomes.instance );

    }

    public static void sendHome( CommandSender sender, String home ) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("SendPlayerHome");
                out.writeUTF(sender.getName());
                out.writeUTF(home);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new PluginMessageTask(b).runTaskAsynchronously(geSuitHomes.instance);
    }

    public static void getHomesList( CommandSender sender ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "GetHomesList" );
            out.writeUTF( sender.getName() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( geSuitHomes.instance );

    }

    public static void importHomes( CommandSender sender ) {
        String path = "plugins/Essentials/userdata";
        File folder = new File( path );

        File[] listOfFiles = folder.listFiles( new FilenameFilter() {
            public boolean accept( File dir, String name ) {
                return name.toLowerCase().endsWith( ".yml" );
            }
        } );

        int userCount = 0;
        int userHomeCount = 0;
        int homeCount = 0;
        for ( File data : listOfFiles ) {
            userCount++;
            FileConfiguration user = YamlConfiguration.loadConfiguration( data );
            if ( user.contains( "homes" ) ) {
                userHomeCount++;
                Set<String> homedata = user.getConfigurationSection( "homes" ).getKeys( false );
                if ( homedata != null ) {
                    for ( String homes : homedata ) {
                        Location loc = new Location( Bukkit.getWorld( user.getString( "homes." + homes + ".world" ) ), user.getDouble( "homes." + homes + ".x" ), user.getDouble( "homes." + homes + ".y" ), user.getDouble( "homes." + homes + ".z" ), ( float ) user.getDouble( "homes." + homes + ".yaw" ), ( float ) user.getDouble( "homes." + homes + ".pitch" ) );
                        setHome( data.getName().substring( 0, data.getName().length() - 4 ), homes, loc );
                        homeCount++;
                    }
                }
            }
        }
        sender.sendMessage( ChatColor.GOLD + "Out of " + userCount + "users, " + userHomeCount + " had homes" );
        sender.sendMessage( ChatColor.GOLD + "Homes imported: " + homeCount );

    }

    public static void reloadHomes( CommandSender sender ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "ReloadHomes" );
            out.writeUTF( sender.getName() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( geSuitHomes.instance );

    }

    public static void setHome( CommandSender sender, String home ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        Location l = ( ( Player ) sender ).getLocation();
        try {
            out.writeUTF( "SetPlayersHome" );
            out.writeUTF( sender.getName() );
            out.writeInt( getServerHomesLimit( sender ) );
            out.writeInt( getGlobalHomesLimit( sender ) );
            out.writeUTF( home );
            out.writeUTF( l.getWorld().getName() );
            out.writeDouble( l.getX() );
            out.writeDouble( l.getY() );
            out.writeDouble( l.getZ() );
            out.writeFloat( l.getYaw() );
            out.writeFloat( l.getPitch() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( geSuitHomes.instance );

    }

    public static void setHome( String sender, String home, Location l ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "SetPlayersHome" );
            out.writeUTF( sender );
            out.writeInt( 100 );
            out.writeInt( 300 );
            out.writeUTF( home );
            out.writeUTF( l.getWorld().getName() );
            out.writeDouble( l.getX() );
            out.writeDouble( l.getY() );
            out.writeDouble( l.getZ() );
            out.writeFloat( l.getYaw() );
            out.writeFloat( l.getPitch() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( geSuitHomes.instance );


    }

    public static int getServerHomesLimit( CommandSender p ) {
        int max = 0;

        int maxlimit = 100;

        if ( p.hasPermission( "gesuit.homes.limits.server.*" ) || p.hasPermission( "gesuit.homes.limits.*" ) ) {
            return maxlimit;
        } else {
            for ( int ctr = 0; ctr < maxlimit; ctr++ ) {
                if ( p.hasPermission( "gesuit.homes.limits.server." + ctr ) ) {
                    max = ctr;
                }
            }

        }

        return max;
    }

    public static int getGlobalHomesLimit( CommandSender p ) {
        int max = 0;

        int maxlimit = 300;

        if ( p.hasPermission( "gesuit.homes.limits.global.*" ) || p.hasPermission( "gesuit.homes.limits.*" ) ) {
            return maxlimit;
        } else {
            for ( int ctr = 0; ctr < maxlimit; ctr++ ) {
                if ( p.hasPermission( "gesuit.homes.limits.global." + ctr ) ) {
                    max = ctr;
                }
            }

        }
        return max;
    }
}
