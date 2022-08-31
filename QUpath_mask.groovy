def newClass = getPathClass("artefact") 

getAnnotationObjects().each { 
         annotation -> annotation.setPathClass(newClass)
}



color = getColorRGB(0, 200, 0)  //green
newClass.setColor(color)

//could set new color for a new class
print "Done with settings"


def imageData = getCurrentImageData()

// Define output path (relative to project)
def outputDir = buildFilePath(PROJECT_BASE_DIR, 'export')
mkdirs(outputDir)
def name = GeneralTools.getNameWithoutExtension(imageData.getServer().getMetadata().getName())
def path = buildFilePath(outputDir, name + "-labels.png")

def path_json = buildFilePath(outputDir, name + "-labels.json")

double downsample = 8

def labelServer = new LabeledImageServer.Builder(imageData)
  .backgroundLabel(0, ColorTools.BLACK) // background->black
  .downsample(downsample)    
//  .addLabel('Tumor', 1)      
//  .addLabel('Region*', 255)
  .addLabel('artefact',1)
 // .addLabel('penmark', 2)
//  .addLabel('Other', 3)
  .multichannelOutput(false) // this can be modified for multi-label case
  .build()


writeImage(labelServer, path)

print "Done! === Export mask"


def annotations = getAnnotationObjects()
boolean prettyPrint = true
def gson = GsonTools.getInstance(prettyPrint)


File file = new File(path_json)
file.withWriter('UTF-8') {
    gson.toJson(annotations,it)
}
