window.onload = function(){
  var contenedor = document.getElementById('contenedor');
  contenedor.style.visibility = 'hidden';
  contenedor.style.opacity = '0';
}
Vue.createApp({
    data() {
      return {

        cliente:"",
        cuentas:"",

        tipoDePrestamo:[],

        prestamos:"",
        cuotas:"",
        cuentadeDestino:"",
        monto:"",

        montoMaximo:"",
        interes:""

      }
    },

    created(){
      axios.get("/api/clients/current").then(response =>{
        this.cliente = response.data
        this.cuentas = response.data.accounts
      })

      axios.get("/api/loans").then(response => {
        this.tipoDePrestamo = response.data
        

        this.interesHipotecario = response.data[0].interest
        this.interesPersonal = response.data[1].interest
        this.interesAutomotriz = response.data[2].interest
      })
    },


    methods:{   
      solicitarPrestamo(){
        Swal.fire({
          title: 'Esta seguro de solicitar este prestamo?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Si, Solicitar'
        })
        .then(result => {
          if (result.isConfirmed) {              
            axios.post('/api/loans',{id:this.prestamos, amount:this.monto, payments:this.cuotas, accountDestiny:this.cuentadeDestino})
            .then(response => {
              Swal.fire(
                'Prestamo Solicitado',
                'Prestamo Solicitado Correctamente',
                'success'
              )
              .then(result => {
                location.reload()
              })
            })
            .catch(error => {
              Swal.fire('invalid transaction!' , error.response.data, 'error')
          })
          
        }
        })

      },



      obtenerMontoMaximoyInteres(){
        let aux = [...this.tipoDePrestamo]
        aux = aux.filter((prestamo) =>  this.prestamos == prestamo.id)
       this.montoMaximo = aux[0].maxAmount
        
       let aux1 = [...this.tipoDePrestamo]
        aux1 = aux1.filter((prestamo) =>  this.prestamos == prestamo.id)
       this.interes = aux1[0].interest
    },

      obtenerCuotas(){
          let aux = [...this.tipoDePrestamo]
          aux = aux.filter((prestamo) =>  this.prestamos == prestamo.id)
          return aux[0].payments
          
      },

      cerrarsesion(){
        axios.post('/api/logout').then(response => window.location.href="/web/index.html")
      }
         
    }
    

  }).mount('#app')