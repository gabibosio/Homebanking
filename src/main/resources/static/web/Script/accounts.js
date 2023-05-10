window.onload = function(){
  var contenedor = document.getElementById('contenedor');
  contenedor.style.visibility = 'hidden';
  contenedor.style.opacity = '0';
}

const app =Vue.createApp({
    data() {
      return {

      cuentas:[],
      cliente:"",
      prestamos:[],

      tipodecuenta:""
      }


    },

    created() {
        axios.get("/api/clients/current")
  .then(datos => {
      this.cuentas = datos.data.accounts
      this.cliente = datos.data
      this.prestamos = datos.data.loans


      this.cuentas.sort(function (cuenta1,cuenta2){
        if(cuenta1.id < cuenta2.id){
          return -1
        }
        if(cuenta1.id > cuenta2.id){

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
    },

    methods:{
      cerrarsesion(){
        axios.post('/api/logout').then(response => window.location.href="/web/index.html")
      },

      crearCuentas(){
         Swal.fire({
          title: 'Quiere Crear una Cuenta?',
          icon: 'warning',
          input: 'select',
          inputOptions: {
              Ahorro: 'Cuenta de Ahorro',
              Corriente: 'Cuenta Corriente',
          },
          inputPlaceholder: 'Seleccione el tipo de Cuenta',
          showCancelButton: true,
          confirmButtonText: 'Si, Crear Cuenta',
          inputValidator: (value) => {
            return new Promise((resolve) => {
              if (value === 'Ahorro') {
                this.tipodecuenta = "Ahorro"
                resolve()
              }
              if(value === 'Corriente'){
                this.tipodecuenta = "Corriente"
                resolve()
              }
              if(value === ''){
                this.tipodecuenta = ""
                resolve()
              }
            })
          }
        })
        .then(result => {
          if (result.isConfirmed) {
          axios.post(`/api/clients/current/accounts`,`accountType=${this.tipodecuenta}`)
          .then(response=>{
            Swal.fire(
              'Cuenta Creada',
              'Tu Cuenta ha Sido Creada',
              'success'
            )
            .then(response => {
              location.reload()
            })
          })
        .catch(error => {
          console.log(error);
        })
      }
      })
      },



      eliminarcuentas(id){
        Swal.fire({
          title: 'Quiere Eliminar esta Cuenta?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Si, Eliminar Cuenta'
        }).then((result) => {
          if (result.isConfirmed) {
              axios.patch(`/api/accounts/visibility/${id}`)
              .then(result => {
                Swal.fire(
                  'Cuenta Eliminada',
                  'Tu Cuenta ha Sido Eliminada',
                  'success'
                )
                .then(result =>{
                  window.location.reload()
                })
              })
              .catch(error => {
                Swal.fire('invalid transaction!' , error.response.data, 'error')
              })
          }
        })
      }
    }
  }).mount('#app')