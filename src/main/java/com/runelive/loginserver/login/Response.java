package com.runelive.loginserver.login;

public enum Response {
	NULL(0),
    INITALIZE(1),
    SUCCESS(2),
    SERVER_ALREADY_CONNECTED(3),
    INVALID_PASSWORD(4),
	INVALID_PASSWORD2(5),
	BAD_SESSION(6);

	private final int opcode;

	private Response(int opcode) {
		this.opcode = opcode;
	}

	public int getOpcode() {
		return opcode;
	}
}
