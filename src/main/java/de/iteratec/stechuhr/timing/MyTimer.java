package de.iteratec.stechuhr.timing;

import java.util.TimerTask;

import javax.swing.JLabel;

public class MyTimer extends TimerTask {
	private JLabel timeLabel;
	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;

	public MyTimer(JLabel timeLabel) {
		this.timeLabel = timeLabel;
		String[] times = timeLabel.getText().split(":");
		if (times[0].startsWith("0")) {
			hours = Integer.parseInt(times[0].substring(1, 2));
		} else {
			hours = Integer.parseInt(times[0]);
		}
		if (times[1].startsWith("0")) {
			minutes = Integer.parseInt(times[1].substring(1, 2));
		} else {
			minutes = Integer.parseInt(times[1]);
		}
		if (times[2].startsWith("0")) {
			seconds = Integer.parseInt(times[2].substring(1, 2));
		} else {
			seconds = Integer.parseInt(times[2]);
		}
	}

	@Override
	public void run() {
		seconds++;
		if (seconds == 60) {
			minutes++;
			seconds = 0;
		}
		if (minutes == 60) {
			hours++;
			minutes = 0;
		}

		String hourString = "" + hours;
		if (hourString.length() == 1) {
			hourString = "0" + hourString;
		}
		String minuteString = "" + minutes;
		if (minuteString.length() == 1) {
			minuteString = "0" + minuteString;
		}
		String secondString = "" + seconds;
		if (secondString.length() == 1) {
			secondString = "0" + secondString;
		}

		timeLabel.setText(hourString + ":" + minuteString + ":" + secondString);

	}

}
