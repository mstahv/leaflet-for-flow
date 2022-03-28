window.Vaadin.Flow.leafletConnector = {
    initLazy: function (c) {
        // Check whether the connector was already initialized for the datepicker
        if (c.$connector) {
            return;
        }
        
        c.$connector = {

          setPoint : function(lat, lon) {
            if(!this.marker) {
              this.marker = L.marker([lat, lon],{draggable:true}).addTo(this.mymap);
              this.marker.on('dragend', function(event){
                var marker = event.target;
                var position = marker.getLatLng();
                c.$connector.center();
                c.$server.updatePosition(position.lat, position.lng);
              });
            } else {
              this.marker.setLatLng(L.latLng(lat, lon));
            }
            this.center();
            c.$server.updatePosition(lat,lon);
          },

          center : function() {
            this.mymap.panTo(this.marker.getLatLng());
          },
          
          setEditorContent : function(html) {
            this.editor.setContent(html);
          }
        };
        
        var currentValue = "";

        const pushChanges = function() {
          c.$server.updateValue(currentValue)
        }

        window.setTimeout(function() {

            var mymap = c.$connector.mymap = L.map(c.id).setView([61, 22], 5);

            L.DomUtil.addClass(mymap._container,'crosshair-cursor-enabled');

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: 'Map data © <a href=\"https://openstreetmap.org\">OpenStreetMap</a> contributors',
            maxZoom: 18,
            }).addTo(mymap);

          c.style.cursor = "crosshair";

          function onMapClick(e) {
            c.$connector.setPoint(e.latlng.lat, e.latlng.lng);
          }

          mymap.on('click', onMapClick);


        }, 10);


/*
        var baseconfig =  JSON.parse(customConfig) || {
          height: 500,
          plugins: [
            'advlist autolink lists link image charmap print preview anchor',
            'searchreplace visualblocks code fullscreen',
            'insertdatetime media table contextmenu paste code'
          ],
          toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
        }
        baseconfig['selector'] =  "#" + c.firstChild.id;
        baseconfig['setup'] = function(ed) {
          c.$connector.editor = ed;
          ed.on('setContent', function(e) {
                console.error('Editor content was set');
                currentValue = ed.getContent();
          });
          ed.on('change', function(e) {
                console.error('Editor was changed');
                currentValue = ed.getContent();
          });
          ed.on('blur', function(e) {
            console.error('Editor was blurred');
            currentValue = ed.getContent();
            pushChanges();
          });
        };
*/

    }
}
