/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.mina;

import client.MapleClient;
import constants.ServerConfig;
import handling.SendPacketOpcode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import server.Randomizer;
import tools.MapleAESOFB;
import java.util.concurrent.locks.Lock;
import server.ServerProperties;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.StringUtil;

import static constants.ServerConstants.CustomEncryption;

public class MaplePacketEncoder extends MessageToByteEncoder<Object> {

    //private static byte r = (byte)Randomizer.nextInt();
    @Override
    protected void encode(ChannelHandlerContext chc, Object message, ByteBuf buffer) throws Exception {
        final MapleClient client = (MapleClient) chc.channel().attr(MapleClient.CLIENT_KEY).get();

        if (client != null) {
            final MapleAESOFB send_crypto = client.getSendCrypto();

            //封包輸出
            byte[] input = ((byte[]) message);
            int pHeader = ((input[0]) & 0xFF) + (((input[1]) & 0xFF) << 8);
            String op = SendPacketOpcode.nameOf(pHeader);
            if (ServerConfig.LOG_PACKETS && !SendPacketOpcode.isSpamHeader(SendPacketOpcode.valueOf(op))) {
                int packetLen = input.length;
                String pHeaderStr = Integer.toHexString(pHeader).toUpperCase();
                pHeaderStr = "0x" + StringUtil.getLeftPaddedStr(pHeaderStr, '0', 4);
                String tab = "";
                for (int i = 4; i > op.length() / 8; i--) {
                    tab += "\t";
                }
                String t = packetLen >= 10 ? packetLen >= 100 ? packetLen >= 1000 ? "" : " " : "  " : "   ";
                final StringBuilder sb = new StringBuilder("[發送]\t" + op + tab + "\t包頭:" + pHeaderStr + t + "[" + packetLen/* + "\r\nCaller: " + Thread.currentThread().getStackTrace()[2] */ + "字元]");
                System.out.println(sb.toString());
                sb.append("\r\n\r\n").append(HexTool.toString((byte[]) message)).append("\r\n").append(HexTool.toStringFromAscii((byte[]) message));
                FileoutputUtil.log(FileoutputUtil.Packet_Log, "\r\n\r\n" + sb.toString() + "\r\n\r\n");
            }

            final byte[] unencrypted = new byte[input.length];
            System.arraycopy(input, 0, unencrypted, 0, input.length);
            final byte[] ret = new byte[unencrypted.length + 4];

            final Lock mutex = client.getLock();
            mutex.lock();
            try {
                final byte[] header = send_crypto.getPacketHeader(unencrypted.length);

                send_crypto.crypt(unencrypted);
                System.arraycopy(header, 0, ret, 0, 4);
                System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length);
                if (CustomEncryption) {
                    for (int i = 0; i < ret.length; i++) {
                        ret[i] ^= 0x43;
                    }
                }
                buffer.writeBytes(ret);
            } finally {
                mutex.unlock();
            }
//            System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length);
//            out.write(ByteBuffer.wrap(ret));
        } else {
            byte[] input = (byte[]) message;
            if (CustomEncryption) {
                for (int i = 0; i < input.length; i++) {
                    input[i] ^= 0x43;
                }
            }
            buffer.writeBytes(input);
        }
    }
}
