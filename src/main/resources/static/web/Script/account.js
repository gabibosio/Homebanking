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
        axios.get(`/api/accounts/${id}`)
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

  axios.get("/api/clients/current")
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
           axios.post(`/api/pdf/generate/${id}`,`desde=${this.desde}&hasta=${this.hasta}`,{'responseType': 'blob'})
            .then(response =>{
              let url = window.URL.createObjectURL(new Blob([response.data]))
              let link = document.createElement("a")
              link.href = url;
              link.setAttribute("download", `${this.cuenta}_${this.desde}-${this.hasta}.pdf`)
              document.body.appendChild(link)
              link.click()
            })
          } else if (result.isDenied) {
            Swal.fire('no se guardo', '', 'info')
          }
        })
      },



      cerrarsesion(){
        axios.post('/api/logout').then(response => window.location.href="/web/index.html")
      }
    }
  }).mount('#app')