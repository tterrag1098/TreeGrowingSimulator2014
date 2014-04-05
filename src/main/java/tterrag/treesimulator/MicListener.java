package tterrag.treesimulator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MicListener implements ITickHandler {
	private TargetDataLine line;
	private byte[] buffer;
	private int bufferOffset;
	public double bufferSizeMultiplier = 0.125;
	private boolean isRunning = false;
	
	public void init()
	{
		AudioFormat format = new AudioFormat(8000.0F, 8, 1, true, true);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format );
		if (!AudioSystem.isLineSupported(info)) {
		    return;
		}
		// Obtain and open the line.
		try {
		    line = (TargetDataLine) AudioSystem.getLine(info);
		    line.open(format);
		} catch (LineUnavailableException ex) {
		    return;
		}
		line.start();
		buffer = new byte[(int) (line.getBufferSize() * bufferSizeMultiplier)];
		bufferOffset = 0;
		isRunning = true;
	}
	
	public void shutdown()
	{
		if (isRunning) {
			isRunning = false;
			line.close();
		}
	}
	
	@Override
	public String getLabel() {
		return "treeSimulatorClientTickHandler";
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (isRunning)
		{
			int available = line.available();
			int remaining = buffer.length-bufferOffset;
			int bytesRead = line.read(buffer, bufferOffset, Math.min(remaining, available));
			bufferOffset += bytesRead;
			if (bytesRead <= available) {
				// buffer is full, analyze data
				bufferOffset = 0;
				int sum = 0;
				for (int i = 0; i < buffer.length; i++) {
					sum += buffer[i];
				}
				double avg = sum / buffer.length;
		        double sumMeanSquare = 0.0;
		        for(int i = 0; i < buffer.length; i++) {
		            sumMeanSquare += Math.pow(buffer[i] - avg, 2.0);
		        }
		 
		        double averageMeanSquare = sumMeanSquare / buffer.length;
		        double rms = Math.pow(averageMeanSquare,0.5d) + 0.5;
		        
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = TreeSimulator.CHANNEL;
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(12);
				DataOutputStream stream = new DataOutputStream(byteStream);
				try {
					stream.writeDouble(rms);
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				packet.data = byteStream.toByteArray();
				packet.length = byteStream.size();
				PacketDispatcher.sendPacketToServer(packet);
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
}
