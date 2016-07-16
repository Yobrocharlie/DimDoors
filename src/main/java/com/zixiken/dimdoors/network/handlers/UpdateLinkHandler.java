package com.zixiken.dimdoors.network.handlers;

import com.zixiken.dimdoors.core.PocketManager;
import com.zixiken.dimdoors.network.packets.UpdateLinkPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateLinkHandler implements IMessageHandler<UpdateLinkPacket, IMessage> {
    @Override
    public IMessage onMessage(UpdateLinkPacket message, MessageContext ctx) {
        PocketManager.getLinkWatcher().update(message.getLinkData());

        return null;
    }
}
