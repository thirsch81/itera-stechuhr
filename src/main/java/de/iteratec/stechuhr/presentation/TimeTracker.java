package de.iteratec.stechuhr.presentation;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.iteratec.stechuhr.projectile.ProjectileBooking;
import de.iteratec.stechuhr.projectile.ProjectileUser;
import de.iteratec.stechuhr.timing.MyTimer;

@SuppressWarnings("serial")
public class TimeTracker extends JFrame {

	private final int LABEL_BORDER = 5;

	private boolean isPlaying = false;
	private boolean isPaused = false;

	private JFrame mainFrame;
	private JButton playButton;
	private JButton pauseButton;
	private JButton stopButton;
	private JLabel timeLabel;
	private JTextField comment;
	private JDialog loginDialog;
	private JComboBox<String> projects = new JComboBox<String>();

	private JTextField nameField;
	private JPasswordField passwordField;

	private String user;
	private String password;

	private Timer timer;

	public TimeTracker(String title) {
		super(title);
		setSystemTray();
	}

	public void showTracker() {
		getContentPane().setLayout(new GridBagLayout());

		timeLabel = new JLabel("00:00:00");
		timeLabel.setFont(new Font("Arial", Font.BOLD, 25));
		getContentPane().add(
				timeLabel,
				new GridBagConstraints(0, 0, 3, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(LABEL_BORDER, LABEL_BORDER, LABEL_BORDER,
								LABEL_BORDER), 0, 0));

		getContentPane().add(
				projects,
				new GridBagConstraints(0, 1, 3, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(LABEL_BORDER, LABEL_BORDER, LABEL_BORDER,
								LABEL_BORDER), 0, 0));
		projects.setPreferredSize(new Dimension(330, 30));

		comment = new JTextField();
		comment.setPreferredSize(new Dimension(330, 30));
		getContentPane().add(
				comment,
				new GridBagConstraints(0, 2, 3, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(LABEL_BORDER, LABEL_BORDER, LABEL_BORDER,
								LABEL_BORDER), 0, 0));

		playButton = new JButton(new ImageIcon(getClass().getClassLoader()
				.getResource("play.png")));
		getContentPane().add(
				playButton,
				new GridBagConstraints(0, 3, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(LABEL_BORDER, LABEL_BORDER, LABEL_BORDER,
								LABEL_BORDER), 0, 0));
		playButton.setBorder(BorderFactory.createRaisedBevelBorder());
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});

		pauseButton = new JButton(new ImageIcon(getClass().getClassLoader()
				.getResource("pause.png")));
		getContentPane().add(
				pauseButton,
				new GridBagConstraints(1, 3, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(LABEL_BORDER, LABEL_BORDER, LABEL_BORDER,
								LABEL_BORDER), 0, 0));
		pauseButton.setBorder(BorderFactory.createRaisedBevelBorder());
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});

		stopButton = new JButton(new ImageIcon(getClass().getClassLoader()
				.getResource("stop.png")));
		getContentPane().add(
				stopButton,
				new GridBagConstraints(2, 3, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(LABEL_BORDER, LABEL_BORDER, LABEL_BORDER,
								LABEL_BORDER), 0, 0));
		stopButton.setBorder(BorderFactory.createRaisedBevelBorder());
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setResizable(false);
		getWindows()[0].setLocationRelativeTo(null);
		setVisible(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("clockTray.png")));

		mainFrame = this;

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowIconified(WindowEvent e) {
				mainFrame.setVisible(false);
			}

		});

		getProjects();
	}

	public void setSystemTray() {
		// Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit()
				.getImage(
						getClass().getClassLoader()
								.getResource("clockTray.png")));
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem loginItem = new MenuItem("Anmeldedaten setzen");
		loginItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user != null && password != null) {
					JOptionPane
							.showMessageDialog(mainFrame,
									"Anmeldedaten bereits gesetzt! Zum Neusetzen bitte Anwendung neu starten!");
				} else {
					loginDialog = new JDialog();
					final JPanel contentPane = new JPanel();
					contentPane.setLayout(new GridBagLayout());
					contentPane.add(new JLabel("Benutzername"),
							new GridBagConstraints(0, 0, 1, 1, 0, 0,
									GridBagConstraints.WEST,
									GridBagConstraints.NONE, new Insets(
											LABEL_BORDER, LABEL_BORDER,
											LABEL_BORDER, LABEL_BORDER), 0, 0));
					nameField = new JTextField();
					nameField.addKeyListener(new KeyListener() {

						@Override
						public void keyTyped(KeyEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void keyReleased(KeyEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void keyPressed(KeyEvent e) {
							if (e.getKeyCode() == KeyEvent.VK_ENTER) {
								saveCredentials(nameField.getText(),
										new String(passwordField.getPassword()));
								loginDialog.dispose();
							}

						}
					});
					nameField.setPreferredSize(new Dimension(200, 30));
					contentPane.add(nameField, new GridBagConstraints(1, 0, 1,
							1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(LABEL_BORDER,
									LABEL_BORDER, LABEL_BORDER, LABEL_BORDER),
							0, 0));

					contentPane.add(new JLabel("Passwort"),
							new GridBagConstraints(0, 1, 1, 1, 0, 0,
									GridBagConstraints.WEST,
									GridBagConstraints.NONE, new Insets(
											LABEL_BORDER, LABEL_BORDER,
											LABEL_BORDER, LABEL_BORDER), 0, 0));

					passwordField = new JPasswordField();
					passwordField.addKeyListener(new KeyListener() {

						@Override
						public void keyTyped(KeyEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void keyReleased(KeyEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void keyPressed(KeyEvent e) {
							if (e.getKeyCode() == KeyEvent.VK_ENTER) {
								saveCredentials(nameField.getText(),
										new String(passwordField.getPassword()));
								loginDialog.dispose();
							}

						}
					});
					passwordField.setPreferredSize(new Dimension(200, 30));
					contentPane.add(passwordField, new GridBagConstraints(1, 1,
							1, 1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(LABEL_BORDER,
									LABEL_BORDER, LABEL_BORDER, LABEL_BORDER),
							0, 0));

					JButton save = new JButton("Speichern");
					save.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							saveCredentials(nameField.getText(), new String(
									passwordField.getPassword()));
							loginDialog.dispose();
						}
					});
					contentPane.add(save, new GridBagConstraints(0, 2, 1, 1, 0,
							0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(LABEL_BORDER,
									LABEL_BORDER, LABEL_BORDER, LABEL_BORDER),
							0, 0));

					loginDialog.setContentPane(contentPane);
					loginDialog.pack();
					loginDialog.setLocationRelativeTo(null);
					loginDialog.setVisible(true);
				}
			}
		});

		// Add components to pop-up menu
		popup.add(loginItem);

		MenuItem showTrackerItem = new MenuItem("Stechuhr �ffnen");
		showTrackerItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user != null && password != null) {
					if (mainFrame == null) {
						showTracker();
					} else {
						mainFrame.setVisible(true);
					}
				} else {
					JOptionPane.showMessageDialog(mainFrame,
							"Anmeldedaten noch nicht gesetzt!");
				}
			}
		});
		popup.add(showTrackerItem);
		popup.addSeparator();

		MenuItem closeItem = new MenuItem("Schlie�en");
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		popup.add(closeItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	public void getProjects() {
		final JDialog dialog = new JDialog(this);
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		final JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2,
				false));
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JLabel("Lade Projekte..."), BorderLayout.NORTH);
		contentPane.add(progressBar, BorderLayout.CENTER);
		dialog.setContentPane(contentPane);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.dispose();
		dialog.setUndecorated(true);
		dialog.setVisible(true);
		this.setEnabled(false);

		Runnable runnable = new Runnable() {
			public void run() {
				// do loading stuff in here
				// for now, simulate loading task with Thread.sleep(...)
				projects.removeAllItems();
				ProjectileUser user = new ProjectileUser();
				List<String> projectileProjects = user.getProjectOptionNames(
						TimeTracker.this.user, password);
				for (String project : projectileProjects) {
					projects.addItem(project);
				}
				// when loading is finished, make frame disappear
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						dialog.setVisible(false);
						mainFrame.setEnabled(true);
						mainFrame.setVisible(true);
					}
				});

			}
		};
		new Thread(runnable).start();

	}

	public static void main(String[] args) {
		new TimeTracker("iteraStechuhr");
	}

	public void play() {
		playButton.setBorder(BorderFactory.createLoweredBevelBorder());
		stopButton.setBorder(BorderFactory.createRaisedBevelBorder());
		if (!isPlaying) {
			timer = new Timer();
			timer.schedule(new MyTimer(timeLabel), 0, 1000);
		}
		isPlaying = true;

		projects.setEnabled(false);
	}

	public void pause() {
		if (isPaused) {
			pauseButton.setBorder(BorderFactory.createRaisedBevelBorder());
			timer = new Timer();
			timer.schedule(new MyTimer(timeLabel), 0, 1000);
		} else if (isPlaying) {
			pauseButton.setBorder(BorderFactory.createLoweredBevelBorder());
			timer.cancel();
		}
		isPaused = !isPaused;
	}

	public void stop() {
		playButton.setBorder(BorderFactory.createRaisedBevelBorder());
		pauseButton.setBorder(BorderFactory.createRaisedBevelBorder());
		isPlaying = false;
		isPaused = false;
		timer.cancel();

		String time = timeLabel.getText().substring(0, 5);
		String project = (String) projects.getSelectedItem();
		ProjectileBooking booking = new ProjectileBooking();
		booking.setProjectOptionName(project);
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

		String[] times = time.split(":");
		String hours = times[0];
		if (times[0].startsWith("0")) {
			hours = times[0].substring(1);
		}
		String minutes = times[1];
		if (times[1].startsWith("0")) {
			minutes = times[1].substring(1);
		}
		int actualMinutes = calendar.get(Calendar.MINUTE);
		if (actualMinutes % 15 < 8) {
			actualMinutes = (actualMinutes / 15) * 15;
		} else {
			actualMinutes = (actualMinutes / 15 + 1) * 15;
		}
		calendar.set(Calendar.MINUTE, actualMinutes);
		int actualHours = calendar.get(Calendar.HOUR_OF_DAY);
		if (actualMinutes == 60) {
			actualHours++;
			calendar.set(Calendar.MINUTE, actualMinutes);
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			actualMinutes = 0;
		}

		// booking.setTime(time);
		booking.setComment(comment.getText());
		booking.setEndTime(actualHours + ":" + actualMinutes);

		int parsedMinutes = Integer.parseInt(minutes);
		int parsedHours = Integer.parseInt(hours);
		if (parsedMinutes % 15 < 8) {
			parsedMinutes = (parsedMinutes / 15) * 15;
		} else {
			parsedMinutes = (parsedMinutes / 15 + 1) * 15;
		}
		if (parsedMinutes == 60) {
			parsedHours++;
			parsedMinutes = 0;
		}
		calendar.add(Calendar.HOUR_OF_DAY, parsedHours * -1);
		calendar.add(Calendar.MINUTE, parsedMinutes * -1);
		booking.setStartTime(calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ (parsedMinutes < 10 ? "0" : "")
				+ calendar.get(Calendar.MINUTE));

		ProjectileUser user = new ProjectileUser();
		user.makeBooking(this.user, password, booking);

		projects.setEnabled(true);

		timeLabel.setText("00:00:00");
	}

	public void saveCredentials(String user, String password) {
		this.user = user;
		this.password = password;
	}

}
