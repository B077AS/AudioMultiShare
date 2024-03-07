package github.B077AS.AudioMultiShare.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioRouting {

	private AudioFormat audioFormat;
	private boolean isRunning;
	private TargetDataLine source;
	private SourceDataLine targetOne;
	private SourceDataLine targetTwo;

	public AudioRouting() {
		this.audioFormat = getAudioFormat();
	}

	public void startRouting(TargetDataLine source, SourceDataLine targetOne, SourceDataLine targetTwo) {
		this.source=source;
		this.targetOne=targetOne;
		this.targetTwo=targetTwo;
		this.isRunning = true;

		try {
			source.open(audioFormat);			
			targetOne.open(audioFormat);
			targetTwo.open(audioFormat);

			source.start();
			targetOne.start();
			targetTwo.start();

			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			while (isRunning) {
				int bytesRead = source.read(buffer, 0, buffer.length);

				targetOne.write(buffer, 0, bytesRead);
				targetTwo.write(buffer, 0, bytesRead);
			}

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public void stopRunning() {
		isRunning=false;
		
		source.close();
		source.stop();
		targetOne.close();
		targetOne.stop();
		targetTwo.close();
		targetTwo.stop();
	}

	private AudioFormat getAudioFormat() {

		float sampleRate = 48000.0f;
		int sampleSizeInBits = 16;
		int channels = 1; // Cambia il numero di canali a 1 per mono
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}


}
