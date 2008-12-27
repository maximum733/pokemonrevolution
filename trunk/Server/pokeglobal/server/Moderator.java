/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright © 2007-2008 Pokemon Global Team

 This file is part of Pokemon Global.

 Pokemon Global is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Pokemon Global is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Pokemon Global.  If not, see <http://www.gnu.org/licenses/>.
*/

package pokeglobal.server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import pokeglobal.server.object.PlayerChar;

/**
 * @author Sienide
 *
 * Filters bad language along with Game Moderator commands
 * 
 */
public class Moderator {
	private ArrayList<Filter> CensorList = new ArrayList<Filter>();
	private Map<String, PlayerChar> playerList;

	   /**
	    * Default Constructor which loads bad language database
		*/
	public Moderator()
	{
		loadFilter();
		playerList = ClientHandler.getPlayerList();
		
	}
	
	PlayerChar fetchPlayerGM(String name, PlayerChar GM) {
		for (String key : playerList.keySet())
		{
			if (key.equalsIgnoreCase(name))
			{
				if (!playerList.get(key).isMod() || !playerList.get(key).isGM() || (GM.isGM() && !playerList.get(key).isGM()))
				{
					return playerList.get(key);
				}
				else 
				{
					GM.getIoSession().write("csServer: " + name + " is a GM/Mod!");
					return null;
				}
			}
		}
		GM.getIoSession().write("csServer: " + name + " does not exist!");
		return null;
	}
	
	   /**
	    * Adds a new bad word to the database and restarts the moderating system without a server shutdown.
	    * @param String wordToFilter - badword:goodword , The word to be filtered will be replaced with the good word
		*/
	//Allows mods to add a new word to the filter
	void addFilter(String wordToFilter)
	{
		try
		{	
			if (!wordToFilter.contains(":")) throw new Exception();
			PrintWriter filterWord = new PrintWriter(new FileWriter(
					"res/badwords.txt"));
			for(int i = 0; i < CensorList.size(); i++)
			{
				filterWord.println(CensorList.get(i).getOriginal() + ":" + CensorList.get(i).getNew());
			}
			filterWord.println(wordToFilter);
			//Close PrintWriters
			filterWord.close();
			
			//Reload filter list
			loadFilter();
		}
		catch (Exception e)
		{
			System.out.println("Could not add word to filter database");
		}
	}
	
	   /**
	    * Reloads the moderating database without restarting the server
		*/
	//Reloads the filter list
	void loadFilter()
	{
		try
		{
			CensorList.clear();
			Scanner originalWords = new Scanner(new File("res/badwords.txt"));
			while(originalWords.hasNextLine())
			{
				String s = originalWords.nextLine();
				Filter f = new Filter();
				if(s.indexOf("*") == 0)
					f.set(s.substring(1), true);
				else
					f.set(s, false);
				CensorList.add(f);	
			}
			originalWords.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	   /**
	    * Filters words/commands appropriately
	    * @param String messageSent - The message sent by the player
	    * @param PlayerChar username - The player who sent the message
		*/
	//Returns the filtered word and executes any mods commands sent by mods
	String filter(String messageSent, PlayerChar user)
	{
		String output = "";
		if(GameServer.isSilent() && !user.isMod() && !user.isGM() && !user.isPMod() && !user.isPOK()) {
			return output;
		}
		if(!user.isMod() && !user.isGM() && !user.isPMod())
		{
			output = removeBad(messageSent);
			return output;
		}
		PlayerChar target = null;
		if(user.isPMod() && !user.isMod() && !user.isGM())
		{
			if(messageSent.indexOf("/mute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(5).trim(), user);
				if ((target != null) && !target.isPMod())
				{
					mutePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(5).trim() + " was muted!");
				}
			}
			else if(messageSent.indexOf("/unmute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(7).trim(), user);
				if ((target != null) && !target.isPMod())
				{
					unmutePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(7).trim() + " was unmuted!");
				}
			}
			else if(messageSent.indexOf("/silentmute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(11).trim(), user);
				if ((target != null) && !target.isPMod())
				{
					silentMutePlayer(target);
				}
			}
			else if(messageSent.indexOf("/tempmute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(9).trim(), user);
				if (target != null)
				{
					tempMutePlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was muted!");
				}
			}
			else
			{
				output = removeBad(messageSent);
			}
		}
		
		boolean checkCmdGM = false;
		if(user.isMod() || user.isGM())
		{
			if(messageSent.indexOf("/addWord") == 0)
			{
				addFilter(messageSent.substring(8));
				output = user.getName() + " updated the Moderating System";
			}
			else if(messageSent.indexOf("/kick ") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(5).trim(), user);
				if(target != null) 
				{
					kickPlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(5).trim() + " was kicked!");
				}
			}
			else if(messageSent.indexOf("/kickall") == 0)
			{
				for (String key : playerList.keySet())
				{
					kickPlayer(playerList.get(key));
					user.getMap().sendToAll("csServer: " + messageSent.substring(5).trim() + " was kicked!");
				}
			}
			else if(messageSent.indexOf("/mute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(5).trim(), user);
				if (target != null)
				{
					mutePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(5).trim() + " was muted!");
				}
			}
			else if(messageSent.indexOf("/silentmute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(11).trim(), user);
				if (target != null)
				{
					silentMutePlayer(target);
				}
			}
			else if(messageSent.indexOf("/disable") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(8).trim(), user);
				if (target != null)
				{
					disablePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(8).trim() + " was disabled!");
				}
			}
			else if(messageSent.indexOf("/enable") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(7).trim(), user);
				if (target != null)
				{
					enablePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(7).trim() + " was freed from all moderator restrictions!");
				}
			}
			else if(messageSent.indexOf("/unmute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(7).trim(), user);
				if (target != null)
				{
					unmutePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(7).trim() + " was unmuted!");
				}
			}
			else if(messageSent.indexOf("/freeze") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(7).trim(), user);
				if (target != null)
				{
					freezePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(7).trim() + " was frozen!");
				}
			}
			/*else if(messageSent.indexOf("/trade") == 0)
			{
				for (String key : playerList.keySet())
				{
					if(key.equalsIgnoreCase(messageSent.substring(6).trim()))
					{
							playerFound = true;
							PlayerChar player = playerList.get(key);
							player.addTradePokemon(user.getParty()[0]);
							user.removePokemon(user.getParty()[0]);
							user.getMap().sendToAll("CServer: ");
						break;
					}
				}
				if (!playerFound)
					username.getIoSession().write("CServer: " + messageSent.substring(5).trim() + " does not exist!");
			}*/
			else if(messageSent.indexOf("/unfreeze") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(9).trim(), user);
				if (target != null)
				{
					unfreezePlayer(target);
					user.getMap().sendToAll("csServer: " + messageSent.substring(9).trim() + " was unfrozen!");
				}
			}
			else if(messageSent.indexOf("/who") == 0)
			{
				int playerCount = 0;
				for (String key : playerList.keySet())
				{
					playerCount = playerCount + 1;
					output = output + key + ", ";
				}
				user.getIoSession().write("cs" + playerCount + " Players Online: " + output);
				output = "";
			} else if (messageSent.indexOf("/antilag") == 0) 
			{
				playerList.get(user).getIoSession().close();
			}
			/*else if(messageSent.indexOf("/setsprite") == 0){
				
				user.getName();
				user.setSprite(messageSent.substring(10));
				user.getMap().sendToAll("CServer: GM changed his sprite and needs to reload.");
				
			}*/
			// /pos command gets server to tell your position in chat.
			else if(messageSent.indexOf("/pos") == 0)
			{		
				target = fetchPlayerGM(messageSent.substring(4).trim(), user);
				if (target != null)
				{
					posPlayer(target, user);
				}
			}
			else if(messageSent.indexOf("/teletoplayer") == 0)
			{		
				target = fetchPlayerGM(messageSent.substring(13).trim(), user);
				if (target != null)
				{
					teleToPlayerPlayer(target, user);
				}
			}
			else if(messageSent.indexOf("/getip") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(6).trim(), user);
				if (target != null)
				{
					user.getIoSession().write("csServer: The player " + target.getName() + " ip is " + target.getIoSession().getLocalAddress());
				}
			}
			else if(messageSent.indexOf("/announce") == 0)
			{
				for (PlayerChar p : playerList.values())
					p.getIoSession().write("cs<Server Announcement>: " + messageSent.substring(9).trim());
				
			}
			else if(messageSent.indexOf("/setPOK") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(7).trim(), user);
				if (target != null)
				{
					user.getIoSession().write("cs" + target.getName() + " POK flag set to " + Boolean.toString((target.isPOK() == false)));
					target.setPOK(target.isPOK() == false);
				}
			}
			else if(messageSent.indexOf("/tempmute") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(9).trim(), user);
				if (target != null)
				{
					tempMutePlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was muted!");
				}
			}
			else if(messageSent.indexOf("/tempfreeze") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(11).trim(), user);
				if (target != null)
				{
					tempFreezePlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was frozen!");
				}
			}
			else if(messageSent.indexOf("/tempdisable") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(12).trim(), user);
				if (target != null)
				{
					tempDisablePlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was disabled!");
				}
			}
			else {
				if(!user.isGM())
					output = removeBad(messageSent);
				else
					checkCmdGM = true;
			}
		}

		if(user.isGM() && checkCmdGM)
		{
			if(messageSent.indexOf("/setPMod") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(8).trim(), user);
				if (target != null)
				{
					user.getIoSession().write("cs" + target.getName() + " Player Mod status set to " + Boolean.toString((target.isPMod() == false)));
					target.setPMod(target.isPMod() == false);
				}
			}
			else if(messageSent.indexOf("/setMod") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(7).trim(), user);
				if (target != null)
				{
					user.getIoSession().write("cs" + target.getName() + " Mod status set to " + Boolean.toString((target.isMod() == false)));
					target.setMod(target.isMod() == false);
				}
			}
			else if(messageSent.indexOf("/ban") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(4).trim(), user);
				if (target != null)
				{
					banPlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was banned!");
				}
			}
			else if(messageSent.indexOf("/unban") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(6).trim(), user);
				if (target != null)
				{
					unbanPlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was unbanned!");
				}
			}
			else if(messageSent.indexOf("/blocknone") == 0)
			{
				GameServer.setLockdown(0);
			}
			else if(messageSent.indexOf("/blocknew") == 0)
			{
				GameServer.setLockdown(1);
			}
			else if(messageSent.indexOf("/blocknonPOK") == 0)
			{
				GameServer.setLockdown(2);
			}
			else if(messageSent.indexOf("/blockall") == 0)
			{
				GameServer.setLockdown(3);
			}
			else if(messageSent.indexOf("/silence") == 0)
			{
				GameServer.setSilence(GameServer.isSilent() == false);
			}
			else if(messageSent.indexOf("/ipban") == 0)
			{
				target = fetchPlayerGM(messageSent.substring(6).trim(), user);
				if (target != null)
				{
					ipbanPlayer(target);
					user.getMap().sendToAll("csServer: " + target.getName() + " was ip banned!");
				}
			}
			else if(messageSent.indexOf("/unipban") == 0)
			{
				unipbanPlayer(messageSent.substring(8).trim());
			}
			else
			{
				output = removeBad(messageSent);
			}
		}
		return output;
	}
	
	   /**
	    * Removes all bad language
	    * @param String messageSent
		*/
	String removeBad(String messageSent)
	{
		String temp = messageSent.trim();
		if(temp.length() > 140)
		{
			temp = temp.substring(0, 140);
		}
		StringTokenizer st3 = new StringTokenizer(temp);
		String output = "";
		while(st3.hasMoreTokens())
		{
			String check = st3.nextToken();
			for(int i = 0; i < CensorList.size(); i++)
			{
				if(CensorList.get(i).hasWildCard)
				{
					if(check.toLowerCase().indexOf(CensorList.get(i).getOriginal().toLowerCase()) >= 0)
					{
						check = CensorList.get(i).getNew();
						break;
					}				
				}
				else
				{
					if(check.equalsIgnoreCase(CensorList.get(i).getOriginal()))
					{
						check = CensorList.get(i).getNew();
						break;
					}						
				}
			}
			output = output + check + " ";
		}
		return output;
	}
	
	   /**
	    * Kicks a player from the server and informs the player they have been kicked
	    * @param String playerName - Player's name
		*/
	void kickPlayer(PlayerChar player)
	{
		if (!player.isMod())
		{
			try
			{
				player.getIoSession().write("cmYou have been kicked from Pokemon Global.");
				Thread.sleep(500);
			}
			catch (Exception e)
			{
			}
			finally
			{
				System.out.println(player.getName() + " was kicked!");
				player.getIoSession().close();
			}
		}
	}
	
	void ipbanPlayer(PlayerChar player)
	{
		try
		{
			ClientHandler.addIPBan(player.getIoSession().getRemoteAddress().toString().substring(1).split(":")[0]);
			player.getIoSession().write("cmYou have been banned. You may no longer play Pokemon Global.");
			Thread.sleep(500);
		}
		catch(Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + "(" + player.getIoSession().getRemoteAddress().toString().substring(1).split(":")[0] + ")" + " was ip banned!");
			player.getIoSession().close();
		}
	}
	
	void unipbanPlayer(String ip)
	{
		ClientHandler.removeIPBan(ip);
		System.out.println(ip + " was unbanned!");
	}
	
	   /**
	    * Mutes a player (disables them from using chat) and informs them they have been muted
	    * @param String playername - Player's name
		*/
	void mutePlayer(PlayerChar player)
	{
		try
		{
			player.getIoSession().write("cmYou have been muted. You may no longer use the chat functions.");
			player.setMuted(true);
			player.setMuteExpiration(0);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was muted!");
		}
	}
	
	void tempMutePlayer(PlayerChar player)
	{
		try
		{
			player.getIoSession().write("cmYou have been temp muted. You may no longer use the chat functions.");
			player.setMuted(true);
			player.setMuteExpiration(System.currentTimeMillis() + 3 * 3600000);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was temp muted!");
		}
	}
	
	void silentMutePlayer(PlayerChar player)
	{
		try
		{
			player.setSilentMuted(true);
			player.setMuteExpiration(0);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was muted!");
		}
	}
	
	   /**
	    * Unmutes a player and informs them they have been unmuted
	    * @param String playername - Player's name
		*/
	void unmutePlayer(PlayerChar player)
	{
		try
		{
			player.getIoSession().write("cmYou have been unmuted. You may now use the chat functions again.");
			player.setMuted(false);
			player.setSilentMuted(false);
			player.setMuteExpiration(0);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was unmuted!");
		}
	}
	
	void banPlayer(PlayerChar player)
	{
		try
		{
			player.setBanned(true);
			player.getIoSession().write("cmYou have been banned from using Pokemon Global");
			Thread.sleep(500);
		}
		catch(Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was banned!");
			player.getIoSession().close();
		}
	}
	
	void unbanPlayer(PlayerChar player)
	{
		try
		{
			player.setBanned(false);
			Thread.sleep(500);
		}
		catch(Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was unbanned!");
		}
	}
	
	   /**
	    * Freezes and Mutes a player
	    * @param String playername - Player's name
		*/
	void disablePlayer(PlayerChar player)
	{
		try
		{
			player.setFrozen(true);
			player.setFreezeExpiration(0);
			player.setMuted(true);
			player.setMuteExpiration(0);
			player.setDisableExpiration(0);
			player.getIoSession().write("cmYou have been disabled from using Pokemon Global.");
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was disabled!");
			player.getIoSession().close();
		}
	}
	
	void tempDisablePlayer(PlayerChar player)
	{
		try
		{
			player.setFrozen(true);
			player.setFreezeExpiration(System.currentTimeMillis() + 3 * 3600000);
			player.setMuted(true);
			player.setMuteExpiration(System.currentTimeMillis() + 3 * 3600000);
			player.setDisableExpiration(System.currentTimeMillis() + 3 * 3600000);
			player.getIoSession().write("cmYou have been temp disabled from using Pokemon Global.");
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was temp disabled!");
			player.getIoSession().close();
		}
	}
	
	   /**
	    * Unfreezes and unmutes a player
	    * @param String playername - Player's name
		*/
	void enablePlayer(PlayerChar player)
	{
		try
		{
			player.setFrozen(false);
			player.setMuted(false);
			player.setSilentMuted(false);
			player.setMuteExpiration(0);
			player.setDisableExpiration(0);
			player.setFreezeExpiration(0);
			player.getIoSession().write("cmYou have been freed from all moderator restrictions.");
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was enabled!");
		}
	}
	
	   /**
	    * Disables player movement
	    * @param String playername - Player's name
		*/
	void freezePlayer(PlayerChar player)
	{
		try
		{
			player.getIoSession().write("cmYou have been frozen. You may no longer move. A moderator may require your attention.");
			player.setFrozen(true);
			player.setFreezeExpiration(0);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was frozen!");
		}
	}
	
	void tempFreezePlayer(PlayerChar player)
	{
		try
		{
			player.getIoSession().write("cmYou have been temp frozen. You may no longer move. A moderator may require your attention.");
			player.setFrozen(true);
			player.setFreezeExpiration(System.currentTimeMillis() + 3 * 3600000);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was temp frozen!");
		}
	}
	
		/**
		 * Re-enables player's movement
		 * @param String playername - Player's name
		 */
	void unfreezePlayer(PlayerChar player)
	{
		try
		{
			player.getIoSession().write("cmYou have been unfrozen. You may move again.");
			player.setFrozen(false);
			player.setFreezeExpiration(0);
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println(player.getName() + " was unfrozen!");
		}
	}
	
	void posPlayer(PlayerChar player, PlayerChar username)
	{
		try
		{
			username.getIoSession().write("cs" +
			"------------------------" +
			player.getName() +
			"'s current map position is: " +
			player.getMapX() +
			", " +
			player.getMapY() +
			". " +
			player.getName() +
			"'s current map co-ord's are: " +
			player.getX() +
			", " +
			player.getY() +
			". " +
			"------------------------");

			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println("Found the position for " + player.getName());
		}
	}
	void teleToPlayerPlayer(PlayerChar player, PlayerChar GM)
	{
		try
		{
			GM.setMap(player.getMap());
			GM.setX(player.getX());
			GM.setY(player.getY());
			GM.getIoSession().write("cs" + "You have teleported to player" + player.getName());
			Thread.sleep(500);
		}
		catch (Exception e)
		{
		}
		finally
		{
			System.out.println("Teleported " + GM.getName() + " to " + player.getName());
		}
	}
}

class Filter
{
	private String f;
	boolean hasWildCard;
	
	void set(String o1, boolean wild)
	{
		f = o1;
		hasWildCard = wild;		
	}
	
	String getOriginal()
	{
		return f.substring(0, f.indexOf(':'));
	}
	
	String getNew()
	{
		return f.substring(f.indexOf(':') + 1);
	}
}
