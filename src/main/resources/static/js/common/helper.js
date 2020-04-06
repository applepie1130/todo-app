
/**
 * @format : YYYY.MM.DD HH:mm:ss (2020.04.06 15:54:01)
 */
Handlebars.registerHelper("formatDate", function(timestamp, format) {
	return moment(new Date(timestamp)).format(format);
});

Handlebars.registerHelper("checkedIf", function (condition) {
    return (condition) ? "checked" : "";
});