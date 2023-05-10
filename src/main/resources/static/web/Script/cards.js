window.onload = function(){
  var contenedor = document.getElementById('contenedor');
  contenedor.style.visibility = 'hidden';
  contenedor.style.opacity = '0';
}

Vue.createApp({
    data() {
      return {

        cliente:"",
        tarjetas:[],
        tarjetasdebito: [],
        tarjetascredito: [],
      }
    },

    created() {
      this.cargardatosiniciales()
        
    },



    methods:{


      formatofecha(fecha){
        fecha = fecha.split("-")
        fecha[0] = fecha[0].substring(2,4)
        return fecha[1] + "/" + fecha[0]
      },

      eliminartarjeta(id){
        Swal.fire({
          title: 'Esta seguro de eliminar esta Tarjeta?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Si, Eliminar!'
        }).then((result) => {
          if (result.isConfirmed) {
            axios.patch(`/api/cards/${id}`)
            Swal.fire(
              'Eliminada!',
              'Tu tarjeta ha sido eliminada',
              'success'
            )
            .then(result =>{
              location.reload()
            })
          }
        })
      },

      vencimiento(){
        let date = new Date();
            actual = date.toISOString().split('T')[0]
            this.tarjetas.forEach(card => {
              if(card.thruDate.valueOf() < actual.valueOf()){
                axios.patch(`/api/cards/expired/${card.id}`)
                .then(() => {
                  this.cargardatos()
                })
              }
            })
      },

      cargardatosiniciales(){
        axios.get("/api/clients/current")
        .then(datos => {
          this.cliente = datos.data
          
          this.tarjetas = datos.data.cards
          this.tarjetasdebito = this.tarjetas.filter(card => card.type == "DEBITO")
          this.tarjetascredito = this.tarjetas.filter(card => card.type == "CREDITO")
        }).then(()=> {
          this.vencimiento()
        })

  .catch(function (error) {
  
    console.log(error);
  })
      },

      cargardatos(){
        axios.get("/api/clients/current")
        .then(datos => {
          this.cliente = datos.data
          
          this.tarjetas = datos.data.cards
          this.tarjetasdebito = this.tarjetas.filter(card => card.type == "DEBITO")
          this.tarjetascredito = this.tarjetas.filter(card => card.type == "CREDITO")
        })

  .catch(function (error) {
  
    console.log(error);
  })
      },

      cerrarsesion(){
        axios.post('/api/logout').then(response => window.location.href="/web/index.html")
      }
      
    }
  }).mount('#app')