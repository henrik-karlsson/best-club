package se.radabmk.bestclub.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.slack.SlackComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import se.radabmk.bestclub.processors.BestClubProcessor;
import se.radabmk.bestclub.processors.TournamentTVProcessor;

@Component
public class BestClubRoute extends RouteBuilder {
	@Autowired
	protected Environment env;
	
	@Override
	public void configure() throws Exception {
		// Setup slack
		final SlackComponent slackComponent = (SlackComponent) this.getContext().getComponent("slack");
		final String webhookUrl = env.getProperty("bestclub.slack.webhookUrl");
	    slackComponent.setWebhookUrl(webhookUrl);
		
		from("file://{{bestclub.tp.export}}?antInclude=*.XLSX&move={{bestclub.tp.processed}}/${file:name.noext}-${date:now:yyyyMMddHHmmss}.${file:ext}&readLockMinAge=10s&delay=30s").
	    	log("File: ${header.CamelFileName}").
	    	process(new BestClubProcessor()).
	    	log("${body}").
	    	wireTap("direct:bestclub-ttv").
			to("direct:bestclub-html");
		
		from("direct:bestclub-ttv").
			process(new TournamentTVProcessor()).
			log("${body}").
			to("freemarker:freemarker/tournament-tv.ftl").
			setHeader(Exchange.FILE_NAME, simple("{{bestclub.ttv.filename}}")).
			to("file://{{bestclub.ttv.output}}?charset=iso-8859-1").
			log("File written: ${header.CamelFileNameProduced}").
			setBody().simple("Bästa klubb poängen uppdaterat! Ladda om TournamentTV filen mf2020.TV för att visa det!").
			to("slack:{{bestclub.slack.channel}}");
		
		
		from("direct:bestclub-json").
			marshal().json(JsonLibrary.Jackson).
			setHeader(Exchange.FILE_NAME, simple("bestclub.json")).
			to("file://target");
		
		from("direct:bestclub-html").
			log("${body}").
			to("freemarker:freemarker/bestclub-html.ftl").
			setHeader(Exchange.FILE_NAME, simple("bestclub.html")).
			to("file://{{bestclub.html.output}}").
			log("File written: ${header.CamelFileNameProduced}");

	}

}
