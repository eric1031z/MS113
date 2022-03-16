package tools.packet;

import handling.RecvPacketOpcode; 
import handling.SendPacketOpcode; 

import java.io.FileWriter; 
import java.io.PrintWriter; 

import constants.ServerConstants; 

/** 
 * 
 * @author SharpAceX (Alan) 
 */ 

public class PacketDefinitionsCreator { 

    private static final int BUILD = ServerConstants.MAPLE_VERSION; 
    private static final int LOCALE = 8; 

    public static void main(String[] args) { 
        try { 
            PrintWriter writer = new PrintWriter(new FileWriter("PacketDefinitions.xml")); 
            writer.println("<ArrayOfDefinition xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"); 
            printDefinition(writer, (1 << 16) - 1, "Maple Handshake", false, false); 
            printSendDefinitions(writer); 
            printRecvDefinitions(writer); 
            writer.print("</ArrayOfDefinition>"); 
            writer.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 

    private static void printSendDefinitions(PrintWriter writer) { 
        for (SendPacketOpcode opcode : SendPacketOpcode.values()) { 
            printDefinition(writer, opcode.getValue(), opcode.toString(), false, isIgnored(opcode)); 
        } 
    } 

    private static void printRecvDefinitions(PrintWriter writer) { 
        for (RecvPacketOpcode opcode : RecvPacketOpcode.values()) { 
            printDefinition(writer, opcode.getValue(), opcode.toString(), true, isIgnored(opcode)); 
        } 
    } 

    private static void printDefinition(PrintWriter writer, int op, String name, boolean outbound, boolean ignore){ 
        if (op < 0) { 
            return; 
        } 
        writer.println("  <Definition>"); 
        writer.println("    <Build>" + BUILD + "</Build>"); 
        writer.println("    <Locale>" + LOCALE + "</Locale>"); 
        writer.println("    <Outbound>" + (outbound ? "true" : "false") + "</Outbound>"); 
        writer.println("    <Opcode>" + op + "</Opcode>"); 
        writer.println("    <Name>" + name + "</Name>"); 
        writer.println("    <Ignore>" + (ignore ? "true" : "false") + "</Ignore>"); 
        writer.println("  </Definition>"); 
    } 
     
    private static boolean isIgnored(SendPacketOpcode header) { 
        switch (header) { 
        case PING: 
            return true; 
        default: 
            return false; 
        } 
    } 
     
    private static boolean isIgnored(RecvPacketOpcode header) { 
        switch (header) { 
        case PONG: 
            return true; 
        default: 
            return false; 
        } 
    } 
}  