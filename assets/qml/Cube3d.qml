import VPlayApps 1.0
import QtQuick 2.9
// 3d imports
import QtQuick.Scene3D 2.0
import Qt3D.Core 2.0
import Qt3D.Render 2.0
import Qt3D.Input 2.0
import Qt3D.Extras 2.0
import QtSensors 5.9

App {

  licenseKey: "A695509703CF59DC151A1806A3629C9FAA5FA097DDD2B0385B6DDB905C4BC0662F5F73AEDB191B5BB8631296395C02FFB474E24349D2A4FA6F73801D72404DFB8AF123F43EF383DB9C8C41356E0058A532E2F0650DCF940DD4A7C4BF63CE3FB0E55A83F17E0CA3F3CCFAD3357BEDC4ED98B2FF4CD267DE126E38F56A74CBD1D192CD26C7165EE0C0DCFBC634A58E601507FD554B1213FCE10A4895B62B3CEC8066C124A2BCE4095D0BC2FBFF5072F0356392C802DAD1410FC0550D42BC419276C0E86C405CF3E5930007F067C68256B0EE80A7975AFA7F57CF0C5414755AFF51E4DFEC9E29333B9BA59070B7DD6ED14C2C65E5D32187F80195322D7EE99874C872EE442C7C303E6AF2F4F2E378A654904E33531B3389C18D1C14EDB1F269F03C8C1C269E12831A72063FD1B23507A772D0EC52ED5FAB16FBB81DAD3AEFDD6FD1"

  // We copy reading to custom property to use behavior on it
  property real xRotation: 0
  property real yRotation: 0

  // We animate property changes for smoother movement of the cube
  Behavior on xRotation { NumberAnimation { duration: 200 } }
  Behavior on yRotation { NumberAnimation { duration: 200 } }

  RotationSensor {
    id: sensor
    active: true

    onReadingChanged: {
      if(reading) {
        xRotation = reading.x * 2
        yRotation = reading.y * 2
      }
    }
  }

  Page {
    title: "3D Cube on Page"
    backgroundColor: Theme.secondaryBackgroundColor

    Column {
      padding: dp(15)
      spacing: dp(5)

      AppText {
        text: "x-axis " + xRotation.toFixed(2)
      }
      AppText {
        text: "y-axis " + yRotation.toFixed(2)
      }
    }

    // 3d object on top of camera
    Scene3D {
      id: scene3d
      anchors.fill: parent
      focus: true
      aspects: ["input", "logic"]
      cameraAspectRatioMode: Scene3D.AutomaticAspectRatio

      Entity {

        // The camera for the 3d world, to view our cube
        Camera {
          id: camera3D
          projectionType: CameraLens.PerspectiveProjection
          fieldOfView: 45
          nearPlane : 0.1
          farPlane : 1000.0
          position: Qt.vector3d(0.0, 0.0, 40.0)
          upVector: Qt.vector3d(0.0, 1.0, 0.0)
          viewCenter: Qt.vector3d(0.0, 0.0, 0.0)
        }

        components: [
          RenderSettings {
            activeFrameGraph: ForwardRenderer {
              camera: camera3D
              clearColor: "transparent"
            }
          },
          InputSettings { }
        ]

        DiffuseMapMaterial {
            id: material
            diffuse: TextureLoader { source: "../img/felgo-logo-white.png" }
            ambient: "white"
            specular: "black"
            shininess: 0
        }

        // The 3d mesh for the cube
        CuboidMesh {
          id: cubeMesh
          xExtent: 8
          yExtent: 8
          zExtent: 8
        }

        // Transform (rotate) the cube depending on sensor reading
        Transform {
          id: cubeTransform
          // Create the rotation quaternion from the sensor reading
          rotation: fromAxesAndAngles(Qt.vector3d(1,0,0), xRotation, Qt.vector3d(0,1,0), yRotation)
        }

        // The actual 3d cube that consist of a mesh, a material and a transform component
        Entity {
          id: sphereEntity
          components: [ cubeMesh, material, cubeTransform ]
        }
      }
    } // Scene3D

    // Allow rotating cube with touch:
    MouseArea {
      anchors.fill: parent
      enabled: !sensor.active

      property point lastPos

      onPressed: {
        lastPos = Qt.point(mouseX, mouseY)
      }
      onPositionChanged: {
        var pos = Qt.point(mouseX, mouseY)
        console.log("pos changed", pos, lastPos)

        // drag 1 inch = 360° rotation
        var factor = dp(160) / 360
        yRotation += (pos.x - lastPos.x) * factor
        xRotation += (pos.y - lastPos.y) * factor

        lastPos = pos
      }
    }

    AppCheckBox {
      anchors.horizontalCenter: parent.horizontalCenter
      anchors.bottom: colorSelRow.top
      text: "Use sensors"
      checked: true
      onCheckedChanged: sensor.active = checked
    }

    // Color selection row
    Row {
      id: colorSelRow
      anchors.horizontalCenter: parent.horizontalCenter
      anchors.bottom: parent.bottom
      spacing: dp(5)
      padding: dp(15)

      Repeater {
        model: [Theme.tintColor, "red", "green", "#FFFF9500", "white"]

        Rectangle {
          color: modelData
          width: dp(48)
          height: dp(48)
          radius: dp(5)

          MouseArea {
            anchors.fill: parent
            onClicked: {
              material.ambient = modelData
            }
          }
        }
      }
    }
  } // Page
} // App
