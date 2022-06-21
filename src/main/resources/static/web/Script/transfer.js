window.onload = function(){
  var contenedor = document.getElementById('contenedor');
  contenedor.style.visibility = 'hidden';
  contenedor.style.opacity = '0';
}
Vue.createApp({
    data() {
      return {

        cliente:"",
        cuenta:[],

        cuentaselect:"",

       cuentaDestino:"",
       cuentaOrigen:"",
       monto:"",
       descripcion:""
      }
    },

    created(){
      axios.get("http://localhost:8080/api/clients/current").then(response =>{
        this.cliente = response.data
        this.cuenta = response.data.accounts
      })

  
    },


    methods:{   
      creartransfearencia(){
            Swal.fire({
              title: 'Esta seguro de realiza esta transferencia?',
              icon: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#3085d6',
              cancelButtonColor: '#d33',
              confirmButtonText: 'Si, Transferir'
            }).then((result) => {
              if (result.isConfirmed) {              
                  axios.post('/api/transactions',`description=${this.descripcion}&accountOrigin=${this.cuentaOrigen}&accountDestiny=${this.cuentaDestino}&amount=${this.monto}`,
                  {headers:{'content-type': 'application/x-www-form-urlencoded'}})
                  .then(response => {
                    Swal.fire(
                      'Transferencia Realizada',
                      'Transferencia realizada correctamente',
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


      cerrarsesion(){
        axios.post('/api/logout').then(response => window.location.href="http://localhost:8080/web/index.html")
      }
         
    }
    

  }).mount('#app')