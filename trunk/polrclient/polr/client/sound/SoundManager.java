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

package polr.client.sound;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/*
 * The SoundManager is how most audio is loaded, organized, and manipulated
 */
public class SoundManager {
	private HashMap<String, Music> channels;
	private HashMap<String, String> fileList;
	private ArrayList<String> channelList;
	private boolean muted = false;

	public SoundManager() {
		channels = new HashMap<String, Music>();
		channelList = new ArrayList<String>();
		fileList = new HashMap<String, String>();
		loadFileList();
	}

	// load the index file for audio samples
	private void loadFileList() {
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(
							"res/audio/index.txt")));

			String f = null;
			while ((f = is.readLine()) != null) {
				String[] addFile = f.split(":", 2);
				fileList.put(addFile[0], addFile[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playChannel(String key, String channel) {
		if (!channels.containsKey(channel)) {
			channels.put(channel, null);
			channelList.add(channel);
		}
		Music currChannel = channels.get(channel);
		try {
			currChannel = new Music("res/audio/" + fileList.get(key), true);
			channels.put(channel, currChannel);
			if (muted)
				channels.get(channel).setVolume(0);
			else
				channels.get(channel).setVolume(1);
			channels.get(channel).play();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void playChannel(String key, String channel, boolean loop) {
		if (!channels.containsKey(channel)) {
			channels.put(channel, null);
			channelList.add(channel);
		}
		Music currChannel = channels.get(channel);
		try {
			currChannel = new Music("res/audio/" + fileList.get(key), true);
			channels.put(channel, currChannel);
			if (muted)
				channels.get(channel).setVolume(0);
			else
				channels.get(channel).setVolume(1);
			channels.get(channel).loop();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// stops the current channel
	public void stopChannel(String channel) {
		if (channels.containsKey(channel) && (channels.get(channel) != null)) {
			channels.get(channel).stop();
		}
	}

	// pauses the current channel
	public void pauseChannel(String channel) {
		if (channels.containsKey(channel) && (channels.get(channel) != null)) {
			channels.get(channel).pause();
		}
	}

	// resumes a previously paused channel
	public void resumeChannel(String channel) {
		if (channels.containsKey(channel) && (channels.get(channel) != null)) {
			if (muted)
				channels.get(channel).setVolume(0);
			else
				channels.get(channel).setVolume(1);
			channels.get(channel).resume();
		}
	}

	// creates a channel
	public void createChannel(String channel) {
		if (!channels.containsKey(channel)) {
			channels.put(channel, null);
			channelList.add(channel);
		}
	}

	public void muteAll() {
		for (String t : channelList) {
			channels.get(t).setVolume(0);
		}
		mute(true);
	}

	public void unmuteAll() {
		for (String t : channelList) {
			channels.get(t).setVolume(1);
		}
		mute(false);
	}

	public void mute(boolean m) {
		muted = m;
	}
}
