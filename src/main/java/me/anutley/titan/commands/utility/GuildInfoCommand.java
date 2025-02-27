package me.anutley.titan.commands.utility;

import me.anutley.titan.commands.Command;
import me.anutley.titan.util.enums.EmbedColour;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.TimeFormat;

public class GuildInfoCommand extends Command {

    public static CommandData GuildInfoCommandData = new CommandData("guildinfo", "Gives information about the current guild");

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("guildinfo")) return;

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor("Owner: " + event.getGuild().getName(), null, event.getGuild().getIconUrl())
                .setTitle("Guild Info")
                .setThumbnail(event.getGuild().getIconUrl())
                .setColor(EmbedColour.NEUTRAL.getColour())
                .addField("Members", String.valueOf(event.getGuild().getMembers().size()), true)
                .addField("Roles", String.valueOf(event.getGuild().getRoles().size()), true)
                .addField("Emotes", String.valueOf(event.getGuild().getEmotes().size()), true)
                .addField("Verification Level", event.getGuild().getVerificationLevel().toString(),true)
                .addField("Created", TimeFormat.RELATIVE.format(event.getGuild().getTimeCreated()), true)
                .addField("Categories", String.valueOf(event.getGuild().getCategories().size()), true)
                .addField("Text Channels", String.valueOf(event.getGuild().getTextChannels().size()), true)
                .addField("Voice Channels", String.valueOf(event.getGuild().getVoiceChannels().size()), true)
                .addField("Stage Channels", String.valueOf(event.getGuild().getStageChannels().size()), true)
                .addField("Boosters", String.valueOf(event.getGuild().getBoosters().size()), true)
                .addField("Boosters Count", String.valueOf(event.getGuild().getBoostCount()), true)
                .addField("Boost Level", String.valueOf(event.getGuild().getBoostTier()), true);


        if (event.getGuild().getBoostRole() != null)
            builder.addField("Boost Role", event.getGuild().getBoostRole().getAsMention(), true);


        event.replyEmbeds(builder.build()).queue();

    }


    @Override
    public String getCommandName() {
        return "guildinfo";
    }

    @Override
    public String getCommandDescription() {
        return "Gives information about the current guild";
    }

    @Override
    public String getCommandUsage() {
        return "/guildinfo";
    }
}
