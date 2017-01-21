package com.runelive.loginserver;

import com.neo.net.packet.PacketInformation;

public final class DecoderConstants {

	public static final PacketInformation[] PACKET_SIZES = new PacketInformation[] {
			PacketInformation.create(0),//0
			PacketInformation.create(PacketInformation.MEDIUM_SIZE, 0, 8388607),//1
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//2
			PacketInformation.create(9),//3
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//4
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//5
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//6
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//7
			PacketInformation.create(2),//8
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//9
			PacketInformation.create(10),//10
			PacketInformation.create(8),//11
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//12
			PacketInformation.create(17),//13
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//14
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//15
			PacketInformation.create(16),//16
			PacketInformation.create(17),//17
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),//18
			PacketInformation.create(PacketInformation.SHORT_SIZE, 0, 65535),// 19
			PacketInformation.create(9),//20
			PacketInformation.create(8),//21
			PacketInformation.create(PacketInformation.MEDIUM_SIZE, 0, 8388607),//22
			PacketInformation.create(PacketInformation.MEDIUM_SIZE, 0, 8388607),//23
			PacketInformation.create(10),//24
			PacketInformation.create(0),//25
			PacketInformation.create(0),//26
			PacketInformation.create(0),//27
			PacketInformation.create(0),//28
			PacketInformation.create(0),//29
			PacketInformation.create(0),//30
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 40
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 50
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 60
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 60
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 70
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 80
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 90
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 100
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 110
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 120
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 130
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 140
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 150
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 160
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 170
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 180
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 190
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 200
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 210
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 220
			PacketInformation.create(0), PacketInformation.create(0), PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 230
			PacketInformation.create(0), PacketInformation.create(0), PacketInformation.create(0),
			PacketInformation.create(0), PacketInformation.create(0), PacketInformation.create(0),
			PacketInformation.create(0), PacketInformation.create(0),
			PacketInformation.create(0),
			PacketInformation.create(0),// 240
			PacketInformation.create(0), PacketInformation.create(0), PacketInformation.create(0),
			PacketInformation.create(0), PacketInformation.create(0), PacketInformation.create(0) // 250
	};
}
