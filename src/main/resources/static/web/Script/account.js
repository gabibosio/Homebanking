window.onload = function(){
  var contenedor = document.getElementById('contenedor');
  contenedor.style.visibility = 'hidden';
  contenedor.style.opacity = '0';
}

Vue.createApp({
    data() {
        return {          
            transacciones:[],
            cliente:"",
            cuenta:"",

            desde:"",
            hasta:"",

            idCuenta:""
        }
        
        
    },
    
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        axios.get(`https://homebakingmindhub.herokuapp.com/api/accounts/${id}`)
        .then(datos => {
          this.cuenta = datos.data.number
          this.idCuenta = datos.data
      this.transacciones = datos.data.transactions
      this.transacciones.sort(function (transaccion1,transaccion2){
        if(transaccion1.id > transaccion2.id){
          return -1
        }
        if(transaccion1.id < transaccion2.id){

          return 1
        }
        else{
          return 0
        }
      })
  })
  .catch(function (error) {
  
    console.log(error);
  })

  axios.get("https://homebakingmindhub.herokuapp.com/api/clients/current")
  .then(datos => {
      this.cliente = datos.data
  })
  .catch(function (error) {
  
    console.log(error);
  })
    },

    methods:{


      descargarPdf(id){
        Swal.fire({
          title: 'Quiere guardar este archivo Pdf?',
          showDenyButton: true,
          showCancelButton: true,
          confirmButtonText: 'Guardar',
          denyButtonText: `No guardar`,
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.fire('Guardado!', '', 'success')
            axios.post(`https://homebakingmindhub.herokuapp.com/api/pdf/${id}`,`desde=${this.desde}&hasta=${this.hasta}`)
          } else if (result.isDenied) {
            Swal.fire('no se guardo', '', 'info')
          }
        })
      },



      cerrarsesion(){
        axios.post('https://homebakingmindhub.herokuapp.com/api/logout').then(response => window.location.href="https://homebakingmindhub.herokuapp.com/index.html")
      }
    }
  }).mount('#app')