/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright � 2007-2008 Pokemon Global Team

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

package polr.server;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import polr.server.object.PlayerChar;
/*
 * This handles all commands accessible to all users that can be used via the chatbox.
 */
public class ChatCommands {
	private ArrayList<Filter> reportList = new ArrayList<Filter>();
	private Map<String, PlayerChar> playerList;
	private CharSequence Magma;
	private CharSequence Rocket;
	private CharSequence Aqua;
	private CharSequence Galactic;
	private CharSequence Team;
	
	public ChatCommands()
	{
		playerList = ClientHandler.getPlayerList();
	}
	
	PlayerChar fetchPlayer(String name) {
		for (String key : playerList.keySet())
		{
			if (key.equalsIgnoreCase(name))
			{
				return playerList.get(key);
			}
		}
		return null;
	}
	
	void report(String messageToReport){
		try
		{	
			PrintWriter report = new PrintWriter(new FileWriter(
					"res/reports.txt"));
			for(int i = 0; i < reportList.size(); i++)
			{
				report.println(reportList.get(i).getOriginal() + ":" + reportList.get(i).getNew());
			}
			report.println(messageToReport);
			//Close PrintWriters
			report.close();
			
		}
		catch (Exception e)
		{
			System.out.println("Could not finish report.");
		}
	}
	
	 String filter(String messageSent, PlayerChar user)
     {
            String output = messageSent;
            PlayerChar target = null;
 			if(messageSent.indexOf("/report") == 0){
 				report(messageSent.substring(7));
 				output = user.getName() + " report succesful.";
 			} else if(messageSent.indexOf("/w ") == 0){
 				if (messageSent.split(" ").length > 2) {
 					target = fetchPlayer(messageSent.split(" ")[1]);
 					if (target != null)
 						sendWhisper(target, user, messageSent.split(" ")[2]);
 					else
 						user.getIoSession().write("csServer: " + messageSent.split(" ")[1] + " does not exist!");
 				}
 				output = "";
 			}
             else if(messageSent.indexOf("/whisper") == 0)
             {
            	 if (messageSent.split(" ").length > 2) {
            		 target = fetchPlayer(messageSent.split(" ")[1]);
            		 if (target != null)
            			 sendWhisper(target, user, messageSent.split(" ")[2]);
            		 else
            			 user.getIoSession().write("csServer: " + messageSent.split(" ")[1] + " does not exist!");
            	 }
            	 output = "";
             }
		else if(messageSent.indexOf("/emote") == 0)
		{
			//for future use
			output = messageSent;
		}
		else if(messageSent.indexOf("/playercount") == 0)
		{
			int playerCount = 0;
			for (String key : playerList.keySet())
			{
				playerCount = playerCount + 1;
			}
			playerList.get(user.getName()).getIoSession().write("cs" + playerCount + " Player(s) Online");
			output = "";
		}
             /*Create team
      		 *Adds team tag and founder to account.
      		 *Takes 10000 pokedollars 
      		 **/
 		/*else if(messageSent.indexOf("/createteam") == 0){
 			if(username.getTeamTag() == null){
 				if(username.isTeamFounder() == false){
 					if(username.getMoney() > 10000){
 						if(playerList.containsValue(messageSent.substring(11))){
 							username.getIoSession().write("CYour team name is already taken.");
 						}else{
 							username.setTeamTag(messageSent.substring(11));
							if(username.getTeamTag().contains(Magma) || username.getTeamTag().contains(Rocket) || username.getTeamTag().contains(Aqua) || username.getTeamTag().contains(Galactic) || username.getTeamTag().contains(Team)){
 								username.setTeamTag(null);
 								username.getIoSession().write("CYou can't create team wich contains Magma, Rocket, Aqua, Galactic or Team");
 							}else{
 								username.setMoney(username.getMoney() - 10000);
 								username.setTeamFounder(true);
 								username.getIoSession().write("CYou created your team.");
 							}
 						}
 					}else{
 						username.setTeamTag(null);
 						username.setTeamFounder(false);
 						username.getIoSession().write("CYou didnt have enough money to pay for team creation.");
 					}
 				}
 			}
 		}
         /*Disbands your team, needs to kick the members first
      		*Removes team tag
      		*removes founder
      		*have to be founder to use the command
      		*/
      		/*else if(messageSent.indexOf("/disbandteam") == 0){
      			if(username.isTeamFounder() == true){
      				username.setTeamTag(null);
      				username.setTeamFounder(false);
      				username.getIoSession().write("CYou disbanded your team.");
      			}
      		}
      		/* Adds team member
      		 * must have founder true
      		 * 
      		 */
      		/*else if(messageSent.indexOf("/addmember") == 0){
      			if(username.isTeamFounder() == true || username.isTeamTm() == true || username.isTeamTa() == true){
      				for(String key : playerList.keySet()){
          				if(key.equalsIgnoreCase(messageSent.substring(10).trim())){
          					PlayerChar player = playerList.get(key);
          					player.setTeamTag(username.getTeamTag());
          					username.getIoSession().write("C" + "You have added member");
          					break;
          				}
          			}
      			}
      		}
      		/*
      		 * Kicks team member
      		 * You have to have founder true to use
      		 */
      		/*else if(messageSent.indexOf("/kickmember") == 0){
      			if(username.isTeamFounder() == true || username.isTeamTm() == true || username.isTeamTa() == true){
      				for(String key : playerList.keySet()){
          				if(key.equalsIgnoreCase(messageSent.substring(10).trim())){
          					PlayerChar player = playerList.get(key);
          					player.setTeamTag(null);
          					username.getIoSession().write("C" + "You have kicked member");
          					break;
          				}
          			}
      			}
      		}
      		/*
      		 * leaves team
      		 * removes team tag
      		 * removes founder rank
      		 * anyone can use.
      		 */
      		/*else if(messageSent.indexOf("/leaveteam") == 0){
      			if(username.getTeamTag() != null){
      				if(username.isTeamFounder() == true){
          				username.getIoSession().write("C You can't leave your team when you are founder of it.");
          			}else{
              			username.setTeamTag(null);
              			username.setTeamTm(false);
              			username.setTeamTa(false);
              			username.getIoSession().write("CYou have left your team.");
          			}
      			}else{
      				username.getIoSession().write("CYou arent in a team.");
      			}
      			

      		}
      		/*
      		 * Gives founder to someone else.
      		 * removes your old founder 
      		 * have to have founder true to use
      		 */
      		/*else if(messageSent.indexOf("/givefounder") == 0){
      			if(username.isTeamFounder() == true){
      				for(String key : playerList.keySet()){
          				if(key.equalsIgnoreCase(messageSent.substring(12).trim())){
          					PlayerChar player = playerList.get(key);
          					if(player.getTeamTag().equals(username.getTeamTag())){
          						player.setTeamFounder(true);
          						username.setTeamFounder(false);
          						player.getIoSession().write("CServer: You have become " + player.getTeamTag() + " founder.");
          						username.getIoSession().write("CServer: You have given your " + username.getTeamTag() + " founder status.");
          						
          					}else{
          						username.getIoSession().write("CServer: You can't give your team founder rank to someone who is not part of the team.");         					}
          				}
          			}
      			}
      			
      		}
 			/*
 			 * Gives Team Administrator status
 			 */
      		/*else if(messageSent.indexOf("/maketa") == 0){
      			if(username.isTeamFounder() == true || username.isTeamTa() == true){
      				for(String key : playerList.keySet()){
      					if(key.equalsIgnoreCase(messageSent.substring(7).trim())){
      						PlayerChar player = playerList.get(key);
      						if(player.getTeamTag().equals(username.getTeamTag())){
      							player.setTeamTa(true);
      							player.getIoSession().write("CServer: You have become " + player.getTeamTag() + " admin");
      							username.getIoSession().write("CServer: You have given admin status to " + player.getName());
      						}else{
      							username.getIoSession().write("CServer: You can't give admin status to someone who is not part of your team.");
      						}
      					}
      				}
      			}
      		}
 			/*
 			 * Takes team Administrator status away.
 			 */
      		/*else if(messageSent.indexOf("/taketa") == 0){
      			if(username.isTeamFounder() == true || username.isTeamTa() == true){
      				for(String key : playerList.keySet()){
      					if(key.equalsIgnoreCase(messageSent.substring(7).trim())){
      						PlayerChar player = playerList.get(key);
      						if(player.getTeamTag().equals(username.getTeamTag())){
      							player.setTeamTa(false);
      							player.getIoSession().write("CServer: Your admin status from " + player.getTeamTag() + " have been taken away by founder");
      							username.getIoSession().write("CServer: You have removed admin status from " + player.getName());
      						}else{
      							username.getIoSession().write("CServer: You can't remove admin status from someone who is not part of your team.");
      						}
      					}
      				}
      			}
      		}
 			/*
 			 * Gives Team Moderator status.
 			 */
      		/*else if(messageSent.indexOf("/maketm") == 0){
      			if(username.isTeamFounder() == true || username.isTeamTa() == true){
      				for(String key : playerList.keySet()){
      					if(key.equalsIgnoreCase(messageSent.substring(7).trim())){
      						PlayerChar player = playerList.get(key);
      						if(player.getTeamTag().equals(username.getTeamTag())){
      							player.setTeamTm(true);
      							player.getIoSession().write("CServer: You have become " + player.getTeamTag() + " moderator");
      							username.getIoSession().write("CServer: You have given moderator status to " + player.getName());
      						}else{
      							username.getIoSession().write("CServer: You can't give moderator status to someone who is not part of your team.");
      						}
      					}
      				}
      			}
      		}
 			/*
 			 * Takes Team Moderators status away.
 			 */
      		/*else if(messageSent.indexOf("/taketm") == 0){
      			if(username.isTeamFounder() == true || username.isTeamTa() == true){
      				for(String key : playerList.keySet()){
      					if(key.equalsIgnoreCase(messageSent.substring(7).trim())){
      						PlayerChar player = playerList.get(key);
      						if(player.getTeamTag().equals(username.getTeamTag())){
      							player.setTeamTm(false);
      							player.getIoSession().write("CServer: Your moderators status from " + player.getTeamTag() + " have been taken away by founder or admin");
      							username.getIoSession().write("CServer: You have removed moderator status from " + player.getName());
      						}else{
      							username.getIoSession().write("CServer: You can't remove moderator status from someone who is not part of your team.");
      						}
      					}
      				}
      			}
      		}*/
     		/*
     		 * Counts how many team members online.
     		 * dont know if works.
     		 */
     		/*else if(messageSent.indexOf("/mo") == 0){
     			int playerCount = 0;
     			for (String key : playerList.keySet())
     			{
     				if (playerList.get(key).getTeamTag() == username.getTeamTag()){
     					playerCount = playerCount + 1;
     					output = output + key + ", ";
     				}
     			}
     			username.getIoSession().write("C" + playerCount + " members online: " + output);
     			output = "";
     		}*/
 			
			/*
			 * Who command, list people online.
			 */
		return output;
	}
	
	void sendWhisper(PlayerChar target, PlayerChar sender, String message)
	{
		if (!target.equals(sender))
		{
			target.getIoSession().write("cw" + "<from: " + sender.getName() + ">" + message);
			sender.getIoSession().write("cw" + "<to: " + target.getName() + ">" + message);
		}
	}

}