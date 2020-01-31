<#ftl output_format="XML" encoding="UTF-8">
<#setting locale="en_US">
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Bästa klubb - ${body.tournament}</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">
  </head>
  <body>
	<div class="page-header">
		<h1>Bästa klubb - ${body.tournament}</h1>
	</div>
	<h2>
		<span class="label label-primary">Uppdaterad: ${body.timestamp?datetime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")?string("yyyy-MM-dd' 'HH:mm")}</span>
	</h2>
	<div class="progress">
		<div class="progress-bar progress-bar-striped" role="progressbar" aria-valuenow="${body.gamesPlayed}" aria-valuemin="0" aria-valuemax="${body.gamesTotal}" style="width: ${body.gamesPlayed/body.gamesTotal*100}%;"><span class="sr-only">${body.gamesPlayed} av ${body.gamesTotal} matcher spelade</span></div>
  	</div>
	<table class="table">
		<thead>
			<tr>
				<th></th>
				<th>Klubb</th>
				<th>Poäng</th>
			</tr>
		</thead>
		<#list body.clubScores>
		<tbody>
			<#items as clubScore>
			<tr>
				<td style="text-align:right;">${clubScore.position}</td>
				<td>${clubScore.name}</td>
				<td style="text-align:right;">${clubScore.points?string("0.0")}</td>
			</tr>
			</#items>
		</tbody>
		</#list>
	</table>
  </body>
</html>