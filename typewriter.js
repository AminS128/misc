// very rudimentary, will probably break in weird ways

// types out the text in elements
// text intended for element should be inside the element, it will get overwritten etc

// load this script last with defer or similar, so that any constructor of the page on DOMContentLoaded will get accounted for
// it does not account for any dynamic content being loaded (yet), it only applies the effect, etc directly on page load
// so putting this script last means that any elements built in DOMContentLoaded by other scripts will get typewritered if needed
// otherwise they will not be :)

const typewriter = {
    className:"typewriter",
    // typeRate:12, // characters per second
    eleRate:6, // rate of elements per second
    maxCPS:250,// max characters per second
    cursor:true,
    init:function(){
        this.eles = Array.from(document.getElementsByClassName(this.className))
        this.eles.forEach((v)=>{
            v.setAttribute("goaltext", v.textContent)
            v.textContent = ""
        })

        this.timer = setInterval(()=>{typewriter.continue()}, 50)
    },
    eles:[],
    timer:null,
    lastUpdate:Date.now(),
    continue:function(){
        // console.log('a')
        if(this.eles.length == 0){clearTimeout(this.timer); return}

        


        // const msPCh = 1000 / this.typeRate // ms per character
        const msPCh = Math.max(1000 / 
            (this.eles[0].getAttribute("goaltext").length * this.eleRate * (this.eles[0].getAttribute("typespeed") || 1)),
            1000 / this.maxCPS)

        const timesince = Date.now() - this.lastUpdate


        if(timesince < msPCh){return}

        if(this.cursor){
            this.eles[0].textContent = this.eles[0].textContent.substring(0, this.eles[0].textContent.length - 2)
        }

        let curtext = this.eles[0].textContent
        let goaltext = this.eles[0].getAttribute('goaltext')

        if(curtext == goaltext){
            this.eles.splice(0, 1); return
        }

        this.lastUpdate = Date.now()

        const ch = Math.round(timesince / msPCh) // round or trunc? arbitrary

        this.eles[0].textContent += goaltext.slice(
            curtext.length, Math.min(curtext.length + ch, goaltext.length)
        )

        if(this.cursor){
            this.eles[0].textContent += "##"
        }

    }
}

document.addEventListener('DOMContentLoaded', ()=>{typewriter.init()})
