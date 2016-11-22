/*
 * Copyright (c) 2016 Lucko (Luck) <luck@lucko.me>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.common.commands.misc;

import me.lucko.luckperms.common.LuckPermsPlugin;
import me.lucko.luckperms.common.commands.CommandResult;
import me.lucko.luckperms.common.commands.SingleCommand;
import me.lucko.luckperms.common.commands.sender.Sender;
import me.lucko.luckperms.common.commands.utils.Util;
import me.lucko.luckperms.common.config.LPConfiguration;
import me.lucko.luckperms.common.constants.Message;
import me.lucko.luckperms.common.constants.Permission;
import me.lucko.luckperms.common.utils.Predicates;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static me.lucko.luckperms.common.commands.utils.Util.formatBoolean;

public class InfoCommand extends SingleCommand {
    public InfoCommand() {
        super("Info", "Print general plugin info", "/%s info", Permission.INFO, Predicates.alwaysFalse(), null);
    }

    @Override
    public CommandResult execute(LuckPermsPlugin plugin, Sender sender, List<String> args, String label) {
        final LPConfiguration c = plugin.getConfiguration();
        Message.INFO.send(sender,
                plugin.getVersion(),
                plugin.getType().getFriendlyName(),
                plugin.getStorage().getName(),
                c.getServer(),
                c.getSyncTime(),
                plugin.getPlayerCount(),
                plugin.getUserManager().getAll().size(),
                plugin.getGroupManager().getAll().size(),
                plugin.getTrackManager().getAll().size(),
                plugin.getStorage().getLog().join().getContent().size(),
                plugin.getUuidCache().getSize(),
                plugin.getLocaleManager().getSize(),
                plugin.getPreProcessContexts(false).size(),
                plugin.getContextManager().getCalculatorsSize(),
                formatBoolean(c.isOnlineMode()),
                formatBoolean(c.isRedisEnabled()),
                formatBoolean(c.isIncludingGlobalPerms()),
                formatBoolean(c.isIncludingGlobalWorldPerms()),
                formatBoolean(c.isApplyingGlobalGroups()),
                formatBoolean(c.isApplyingGlobalWorldGroups()),
                formatBoolean(c.isApplyingWildcards()),
                formatBoolean(c.isApplyingRegex()),
                formatBoolean(c.isApplyingShorthand())
        );

        LinkedHashMap<String, Object> platformInfo = plugin.getExtraInfo();
        if (platformInfo == null || platformInfo.isEmpty()) {
            return CommandResult.SUCCESS;
        }

        Message.EMPTY.send(sender, "&f-  &bPlatform Info:");
        for (Map.Entry<String, Object> e : platformInfo.entrySet()) {
            Message.EMPTY.send(sender, "&f-     &3" + e.getKey() + ": " + formatValue(e.getValue().toString()));
        }

        return CommandResult.SUCCESS;
    }

    private static String formatValue(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Util.formatBoolean(Boolean.parseBoolean(value));
        }

        try {
            int i = Integer.parseInt(value);
            return "&a" + i;
        } catch (NumberFormatException ignored) {}

        return "&f" + value;
    }
}