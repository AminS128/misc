const c = document.getElementById('canvas')
const ctx = c.getContext('2d')

function generate(res = 8, list2 = [], count = 10){

    c.width = c.clientWidth;
    c.height = c.clientHeight

    let list = list2
    if(list.length == 0){
        list = generatePoints(150)
    }   
    
    let resolution = res;


    let rf = (count % 1.5) - Math.max(0, 2*(count % 1.5) - 1.5)
    let gf = ((count-1) % 1.5) - Math.max(0, 2*((count-1) % 1.5) - 1.5)
    let bf = ((count-0.5) % 1.5) - Math.max(0, 2*((count-0.5) % 1.5) - 1.5)

    console.log(rf,gf,bf)

    for(var i = 0; i < c.width/resolution; i ++){
        for(var ii = 0; ii < c.height/resolution;ii++){
            let num = 255*255
            let snum = 255*255
            for(var iii = 0; iii < list.length; iii++){
    
                let dx = list[iii][0]-i*resolution;
                let dy = list[iii][1]-ii*resolution;
                let dist = dx*dx+dy*dy
                if(dist < snum){
                    if(dist < num){
                        snum = num
                        num = dist
                    }else{
                        snum = dist
                    }
                }
            }

            let n = Math.sqrt(num)
            let n2 = Math.sqrt(snum)

            let m1 = 255 - 8*(n2-n)

            let a = Math.abs(
            (500*Math.atan(m1/1505) + 
            (3+0.003*m1)*(n))*0.3
            )
            
            ctx.fillStyle = `rgb(${1.3*a*(rf + 0.2)} ${a*(gf + 0.2)} ${a*(bf + 0.3)})`

            ctx.fillRect(i*resolution, ii*resolution, resolution, resolution)
           
        }
    }
       
}


generate(6)

function generatePoints(amount = 150){
    const list = []
    for(var i = 0; i < amount; i ++){
        list.push([Math.random()*c.width, Math.random()*c.height])
    }
    return list
}



let anim = {
    list:[],
    timer:null,
    resolution:10,
    fps:20,
    duration:30,
    frame:0,
    start(){
        clearTimeout(anim.timer)
        anim.list = generatePoints(70)
        const looplength = anim.duration * anim.fps // seconds * fps
        
        for(var i = 0; i < anim.list.length; i ++){

            const harmonics = 5
            const resonantX = Math.trunc((Math.random()-0.5)*harmonics) 
            const resonantY = Math.trunc((Math.random()-0.5)*harmonics)

            anim.list[i].push(resonantX*c.width*2/looplength)
            anim.list[i].push(resonantY*c.height*2/looplength)
            // anim.list[i].push(Math.random())
        }
        anim.timer = setInterval(anim.iterate, 1000/anim.fps)
    },
    iterate(){
        anim.list.forEach((v)=>{
            v[0]+=v[2];v[1]+=v[3]
            if(v[0]<0){v[2]*=-1;v[0]*=-1}
            if(v[0]>c.width){v[2]*=-1;v[0]=c.width-(v[0]-c.width)}
            if(v[1]<0){v[3]*=-1;v[1]*=-1}
            if(v[1]>c.height){v[3]*=-1;v[1]=c.height-(v[1]-c.height)}
            // v[4]+=0.01
        })// iteratem

        if(anim.frame%(anim.fps*10) == 0){
            console.log(anim.list[0])
        }

        generate(anim.resolution, anim.list, anim.frame * (1.5 * 0.1 / (anim.fps)) + 10)
        
        
        gif.options.width = c.width
        gif.options.height = c.height

        gif.addFrame(ctx, {delay:1000/anim.fps,copy:true})
        
        if(anim.frame >= anim.duration*anim.fps){anim.halt()}

        anim.frame++

    },
    halt(){
        clearTimeout(anim.timer)
        gif.on('finished', (blob)=>{
            console.log(blob)
            window.open(URL.createObjectURL(blob))
        })
        gif.width = c.width
        gif.height = c.height
        gif.render()
    }
}

var gif = new GIF({workers:2, quality:40, dither:'Atkinson'})
