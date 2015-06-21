/**
 * Created by piotrek on 6/20/15.
 */

$(function () {
    var links = [];
    var nodes = {};
    var minValue = Infinity;

    $('#graph-refresh-button').click(refreshGraph);

    refreshGraph();

    function refreshGraph() {
        $.get('http://localhost:8080/keywords/connections', function (data) {
                links = data.map(function (connection) {
                    return {
                        source: connection.firstKeyword,
                        target: connection.secondKeyword,
                        value: connection.totalCount
                    }
                });

                links.forEach(function (link) {
                    link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
                    link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
                });

                links = links.filter(function (link) {
                    return link.value > 0;
                });

                links.forEach(function (link) {
                    if (link.value < minValue) {
                        minValue = link.value;
                    }
                });

                redrawGraph();
            }
        );
    }

    function redrawGraph() {
        var width = 960,
            height = 960;

        var force = d3.layout.force()
            .nodes(d3.values(nodes))
            .links(links)
            .size([width, height])
            .linkDistance(function (link) {
                return height / Math.sqrt(link.value / minValue);
            })
            .charge(-300)
            .on('tick', tick)
            .start();

        d3.select('#graph').select('svg').remove();

        var svg = d3.select('#graph').append('svg')
            .attr('width', width)
            .attr('height', height);

        var link = svg.selectAll('.link')
            .data(force.links())
            .enter().append('line')
            .attr('class', 'link');

        var node = svg.selectAll('.node')
            .data(force.nodes())
            .enter().append('g')
            .attr('class', 'node')
            .on('mouseover', mouseover)
            .on('mouseout', mouseout)
            .call(force.drag);

        node.append('circle')
            .attr('r', 8);

        node.append('text')
            .attr('x', 12)
            .attr('dy', '.35em')
            .attr('class', 'node-text')
            .text(function (d) {
                return d.name;
            });

        var text = svg.selectAll('.edge-text')
            .data(force.links())
            .enter().append('text')
            .attr('x', function (d) {
                return d.source.x + (d.target.x - d.source.x) / 2;
            })
            .attr('y', function (d) {
                return d.source.y + (d.target.y - d.source.y) / 2;
            })
            .text(function (d) {
                return d.value;
            });

        function tick() {
            link
                .attr('x1', function (d) {
                    return d.source.x;
                })
                .attr('y1', function (d) {
                    return d.source.y;
                })
                .attr('x2', function (d) {
                    return d.target.x;
                })
                .attr('y2', function (d) {
                    return d.target.y;
                });

            node
                .attr('transform', function (d) {
                    return 'translate(' + d.x + ',' + d.y + ')';
                });
            text
                .attr('x', function (d) {
                    return d.source.x + (d.target.x - d.source.x) / 2;
                })
                .attr('y', function (d) {
                    return d.source.y + (d.target.y - d.source.y) / 2;
                });
        }

        function mouseover() {
            d3.select(this).select('circle').transition()
                .duration(750)
                .attr('r', 16);
        }

        function mouseout() {
            d3.select(this).select('circle').transition()
                .duration(750)
                .attr('r', 8);
        }
    }
});