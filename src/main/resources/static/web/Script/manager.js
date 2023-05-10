Vue.createApp({
    data() {
      return {
        clientes:[],
        jsonClientes:[],
        nombre:"",
        apellido:"",
        email:"",
        password:"",
        cliente:[],

        nombremodal:"",
        apellidomodal:"",
        emailmodal:"",

        nombrePrestamo:"",
        montoMaximo:"",
        interes:"",
        cuotas:[]
      }
    },

    created(){
      axios.get("/api/clients")
      .then(response =>  {
        this.clientes = response.data
        this.jsonClientes = response.data
  })
  .catch(function (error) {
   
    console.log(error);
  })

    },
    methods:{     
      dibujarclientes(){
        Swal.fire({
          title: 'Estas seguro de crear este cliente?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'si, Crearlo!'
        })
        .then(result => {
          if(result.isConfirmed){
            if(this.nombre !== "" && this.apellido !=="" && this.email !=="" && this.email.includes("@") &&  this.email.includes(".com")){
              axios.post("/clients/create",`firstName=${this.nombre}&lastName=${this.apellido}&email=${this.email}&password=${this.password}`)
              .then(function (response) {
                Swal.fire(
                  'Creado!',
                  'el cliente ha sido creado.',
                  'success'
                )
                .then(()=>{
                  location.reload()
                })
              })
              .catch(error =>{Swal.fire('invalid transaction!' , error.response.data, 'error')
              })
            }
          }
        })
        },


        borrarcliente(cliente){
          Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
          }).then((result) => {
            if (result.isConfirmed) {              
              Swal.fire(
                'Deleted!',
                'Your file has been deleted.',
                'success'
              ).then((result)=>{
                axios.delete(cliente._links.client.href)
                location.reload()
              })
            }
          })
        },

    

        editarcliente(id){
          if(this.nombremodal !== "" && this.apellidomodal !=="" && this.emailmodal !=="" && this.emailmodal.includes("@") &&  this.emailmodal.includes(".com")){
          axios.patch(`/api/clients/modificar/${id}`,`firstName=${this.nombremodal}&lastName=${this.apellidomodal}&email=${this.emailmodal}`)
          .then(function (response) {
            location.reload(response)
          })
        }
      },

     


      crearPrestamo(){
        Swal.fire({
          title: 'Esta Seguro de Crear este Prestamo?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Si, Crear!'
        }).then((result) => {
          if (result.isConfirmed) {
            let cantidadCuotas = this.cuotas.split(",")
            axios.post("/api/createLoans",{name:this.nombrePrestamo, maxAmount:this.montoMaximo, interest:this.interes, payments:cantidadCuotas})
            .then(() => {
              Swal.fire(
                'Creado!',
                'El Prestamo ha Sido Creado',
                'success'
              )
              .then(()=>{
                location.reload()
              })
            })
            .catch(error => {
              Swal.fire('Ocurrio un Error!' , error.response.data, 'error')
            })
          }
        }) 
      },

      cerrarsesion(){
        axios.post('/api/logout').then(response => window.location.href="/web/index.html")
      }
  
      },
    

  }).mount('#app')