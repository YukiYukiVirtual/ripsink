import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.sound.sampled.*;

class Setting
{
	static int windowSize = 256;
	static String path = "./image";
	static String deviceName = null;
	static int mouthLength = 2;
	static int mouthRate = 1;
	static int frameRate = 60;
	static int threshold = 16000;
	static int delay = 0;
	static int bgColor = 0x00ff00;
}

public class Main extends JFrame implements WindowListener, ActionListener
{
	CharacterPanel cpanel;
	Point defaultLocation;
	
	JFrame controlFrame;
	JButton posButton;
	JTextArea ripThreshold;
	JButton thresholdButton;
	JTextArea delayTime;
	JButton delayButton;
	JButton jccButton;
	JLabel statusLabel;
	private Color bgColor = null;
	final String str_stop = "リップシンクを止める";
	final String str_pos  = "位置をリセットする";
	final String str_jcc  = "色を選択";
	
	Main()
	{
		// このフレームの基本設定
		super("結城ゆき");
		setDefaultCloseOperation(EXIT_ON_CLOSE); // 閉じたときの動作
		
		try{
			String src = "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";
			byte[] iconData = Base64.getDecoder().decode(src);
			InputStream bytestream = new ByteArrayInputStream(iconData);
			BufferedImage icon = ImageIO.read(bytestream);
			setIconImage(icon);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		setResizable(false); // リサイズをしない
		addWindowListener(this);
		
		cpanel = new CharacterPanel(
			Setting.mouthLength,
			Setting.mouthRate,
			Setting.frameRate,
			Setting.threshold,
			Setting.delay,
			Setting.deviceName,
			Setting.bgColor,
			Setting.path
		);
		Container cp = getContentPane();
		cp.add(cpanel);
		
		init_controlFrame();
		controlFrame.addWindowListener(this);
		
		// コントロールパネルの下に表示する
		defaultLocation = new Point(0,controlFrame.getHeight());
		setLocation(defaultLocation);
		// このフレームを表示する
		cp.setPreferredSize(new Dimension(Setting.windowSize,Setting.windowSize));
		pack();
		setVisible(true);
		cpanel.start();
	}
	
	void init_controlFrame()
	{
		JFrame f = controlFrame = new JFrame("コントロールパネル");
		// ユーティリティにする（タスクバーに非表示）
		f.setType(Window.Type.UTILITY);
		f.setSize(200,300);
		//f.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container cp = f.getContentPane();
		
		// コントロールパネル(JPanelのほうのパネル)
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5,1));
		cp.add(controlPanel);
		
		// 閾値設定
		JPanel thresholdPanel = new JPanel();
		thresholdPanel.setLayout(new BorderLayout());
		controlPanel.add(thresholdPanel);
		
		JLabel thresholdLabel = new JLabel("閾値");
		thresholdPanel.add("West",thresholdLabel);
		
		ripThreshold = new JTextArea(""+cpanel.voiceThreshold);
		thresholdPanel.add("Center",ripThreshold);
		
		thresholdButton = new JButton("set");
		thresholdButton.addActionListener(this);
		thresholdPanel.add("East",thresholdButton);
		
		// 口パク時間遅延設定
		JPanel delayPanel = new JPanel();
		delayPanel.setLayout(new BorderLayout());
		controlPanel.add(delayPanel);
		
		JLabel delayLabel = new JLabel("遅延");
		delayPanel.add("West",delayLabel);
		
		delayTime = new JTextArea(""+cpanel.delay);
		delayPanel.add("Center",delayTime);
		
		delayButton = new JButton("set");
		delayButton.addActionListener(this);
		delayPanel.add("East",delayButton);
		
		// 背景色設定
		jccButton = new JButton(str_jcc);
		jccButton.addActionListener(this);
		controlPanel.add(jccButton);
		
		// 位置リセットボタン
		posButton = new JButton(str_pos);
		posButton.addActionListener(this);
		controlPanel.add(posButton);
		
		// ステータスバー
		statusLabel = new JLabel();
		controlPanel.add(statusLabel);
		cpanel.statusBar = statusLabel;
		
		f.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		// イベント発火オブジェクトを取得
		Object src = e.getSource();
		
		if(src == thresholdButton)
		{
			// 閾値を設定する
			String text = ripThreshold.getText();
			if(java.util.regex.Pattern.compile("^[0-9]*$").matcher(text).find())
			{
				cpanel.setVoiceThreshold(Integer.parseInt(text));
			}
		}
		else if(src == delayButton)
		{
			// 遅延時間を設定する
			String text = delayTime.getText();
			if(java.util.regex.Pattern.compile("^[0-9]*$").matcher(text).find())
			{
				cpanel.setDelay(Integer.parseInt(text));
			}
			else
			{
				cpanel.setDelay(0);
			}
		}
		else if(src == jccButton)
		{
			// 背景色を選択、設定する
			bgColor = JColorChooser.showDialog(this,"背景色選択",bgColor);
			cpanel.setBGColor(bgColor);
		}
		else if(src == posButton)
		{
			// 顔の位置を戻す
			setLocationRelativeTo(null);
			setLocation(defaultLocation);
			toFront();
		}
	}
	public static void main(String[] args)
	{
		for(String s:args)
		{
			try{
				int colonIndex = s.indexOf(':');
				String key = s.substring(0, colonIndex);
				String value = s.substring(colonIndex + 1);
				if(key.equals("windowSize"))
					Setting.windowSize = Integer.parseInt(value);
				
				else if(key.equals("path"))
					Setting.path = (value);
				
				else if(key.equals("deviceName"))
					Setting.deviceName = (value);
				
				else if(key.equals("mouthLength"))
					Setting.mouthLength = Integer.parseInt(value);
				
				else if(key.equals("mouthRate"))
					Setting.mouthRate = Integer.parseInt(value);
				
				else if(key.equals("frameRate"))
					Setting.frameRate = Integer.parseInt(value);
				
				else if(key.equals("threshold"))
					Setting.threshold = Integer.parseInt(value);
				
				else if(key.equals("delay"))
					Setting.delay = Integer.parseInt(value);
				
				else if(key.equals("bgColor"))
					Setting.bgColor = Integer.parseInt(value, 16);
				
				else
				{
					System.out.println("\""+key+"\"という項目はありません");
					continue;
				}
				
				System.out.println("\""+key+"\"を"+value+"に設定");
			}
			catch(NumberFormatException e)
			{
				System.out.println("\""+s+"\"の数値を正しく変換できませんでした");
			}
			catch(StringIndexOutOfBoundsException e)
			{
				System.out.println("\""+s+"\"の解析に失敗しました");
			}
		}
		new Main();
	}
	
	public void windowClosing(WindowEvent e)
	{
		cpanel.stop();
	}
	
	public void windowIconified(WindowEvent e)
	{
		if(e.getWindow() == this)
		{
			controlFrame.setVisible(false);
		}
	}
	
	public void windowDeiconified(WindowEvent e)
	{
		if(e.getWindow() == this)
		{
			controlFrame.setVisible(true);
		}
	}
	
	public void windowActivated(WindowEvent e)
	{
		if(e.getWindow() == this && e.getOppositeWindow() != controlFrame)
		{
			controlFrame.toFront();
			toFront();
		}
		if(e.getWindow() == controlFrame && e.getOppositeWindow() != this)
		{
			toFront();
			controlFrame.toFront();
		}
	}
	
	public void windowOpened(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	
	public static boolean isInteger(String text)
	{
		return java.util.regex.Pattern.compile("^[1-9][0-9]*$").matcher(text).find();
	}
}

class CharacterPanel extends JPanel implements Runnable
{
	BufferedImage base;
	BufferedImage[] mouth;
	Color bgColor;
	int frameRate;
	boolean rip;
	boolean preOpen = false;
	int mouthCount;
	int mouthRate;
	int currentMouthRate;
	int voiceThreshold;
	int delay;
	int[] vqueue = new int[100];
	JLabel statusBar;
	String deviceName = null;
	
	CharacterPanel(
		int mouthLength,
		int mouthRate,
		int frameRate,
		int threshold,
		int delay,
		String deviceName,
		int rgb,
		String path)
	{
		mouth = new BufferedImage[mouthLength];
		this.mouthRate = mouthRate;
		this.frameRate = frameRate;
		voiceThreshold = threshold;
		this.delay = delay;
		this.deviceName = deviceName;
		bgColor = new Color(rgb);
		try
		{
			base = ImageIO.read(new File(path + "/base.png"));
			for(int i=0;i<mouth.length;i++)
			{
				mouth[i] = ImageIO.read(new File(path + "/"+i+".png"));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(bgColor);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(base, 0, 0, getWidth(),getHeight(),null);
		
		if(vqueue[delay] > voiceThreshold)// 声の大きさが閾値を超えている
		{
			// 口を開けようとする
			mouthCount = Math.min(mouth.length-1, mouthCount+1);
		}
		else
		{
			// 口を閉じようとする
			mouthCount = Math.max(0,mouthCount-2);
		}
		int mouthIndex = mouthCount;
		
		g.drawImage(mouth[mouthIndex], 0, 0, getWidth(), getHeight(), null);
		if(statusBar != null)
		{
			String str = String.format("<html>声の大きさ:%d %s<br>口の大きさ:%d/%d",
				vqueue[delay], (vqueue[delay] > voiceThreshold?"Over":""),
				mouthIndex, mouth.length-1);
			statusBar.setText(str);
		}
	}
	
	public void setBGColor(Color c)
	{
		if(c!=null)
		{
			bgColor = c;
			repaint();
		}
	}
	
	public void setVoiceThreshold(int t)
	{
		voiceThreshold = t;
	}
	
	public void setDelay(int o)
	{
		delay = Math.max(0,Math.min(o,vqueue.length-1));
	}
	public void start()
	{
		rip = true;
		new Thread(this).start();
	}
	
	public void stop()
	{
		rip = false;
	}
	
	private int getLevel(byte[] data)
	{
		int total = 0;
		for(int i=0;i<data.length;i+=2)
		{
			int x = Math.abs((data[i]<<8)+data[i+1]);
			total += x;
		}
		return total/(data.length/2);
	}
	
	public void run()
	{
		int ms = 1000/frameRate;
		int freqency = 8000;
		int bits = 16;
		AudioFormat format = new AudioFormat(freqency, bits, 1, true, false);
		Recorder rec = new Recorder(format, ms/4, deviceName);
		while(rip)
		{
			try
			{
				if(currentMouthRate == 0)
				{
					for(int i=vqueue.length-1;i>=1;i--)
					{
						vqueue[i] = vqueue[i-1];
					}
					vqueue[0] = getLevel(rec.getData());
					repaint();
					Thread.sleep(ms);
					currentMouthRate = mouthRate;
				}
				else
				{
					currentMouthRate--;
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		rec.end();
	}
}
class Recorder implements Runnable
{
	private byte[] data;
	private TargetDataLine line = null;
	private AudioInputStream stream;
	private int ms;
	
	Recorder(AudioFormat format ,int ms ,String deviceName)
	{
		try
		{
			this.ms = ms;
			int framesize = format.getFrameSize();
			float framerate = format.getFrameRate();
			int dataSize = (int)(framesize * framerate * ms) / 1000;
			data = new byte[dataSize];
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			
			//マイクを探す
			if(deviceName != null && !deviceName.equals(""))
			{
				Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
				//Mixer.Infoを辞書順ソート
				Arrays.sort(mixerInfo,new Comparator<Mixer.Info>(){
					@Override public int compare(Mixer.Info a, Mixer.Info b)
					{
						String A = new String(a.getName().getBytes(java.nio.charset.StandardCharsets.ISO_8859_1), java.nio.charset.Charset.forName("Shift-JIS"));
						String B = new String(b.getName().getBytes(java.nio.charset.StandardCharsets.ISO_8859_1), java.nio.charset.Charset.forName("Shift-JIS"));
						return A.indexOf(deviceName) - B.indexOf(deviceName);
					}
				});
				Mixer mixer = null;
				//Mixer.Infoの名前にdeviceNameを含むものを見つけたらTargetDataLineを設定する
				for(int i=0;i<mixerInfo.length;i++)
				{
					Mixer.Info mi = mixerInfo[i];
					String name = new String(mi.getName().getBytes(java.nio.charset.StandardCharsets.ISO_8859_1), java.nio.charset.Charset.forName("Shift-JIS"));
					if(name.indexOf(deviceName) > 0)
					{
						mixer = AudioSystem.getMixer(mi);
						if(!mixer.isLineSupported(new Line.Info(TargetDataLine.class)))
							continue;
						line = (TargetDataLine)mixer.getLine(info);
						break;
					}
				}
			}
			
			//デフォルトマイク
			if(line == null)
			{
				line = (TargetDataLine)AudioSystem.getLine(info);
			}
			
			line.open(format);
			line.start();
			
			stream = new AudioInputStream(line);
			
			new Thread(this).start();
		}
		catch(LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	public void run()
	{
		// 常にデータを取得しつづける
		while(true)
		{
			try
			{
				stream.read(data , 0, data.length);
				Thread.sleep(1);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	public byte[] getData()
	{
		return data ;
	}
	
	public void end()
	{
		line.stop();
		line.close();
		try
		{
			stream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}