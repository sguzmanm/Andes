import React from 'react';
import { View, Text, StyleSheet, Dimensions } from 'react-native';
import { Button, Icon } from 'react-native-elements';
import Carousel from 'react-native-snap-carousel';
import Header from './../../elementos/Header';
import Screen from './../../elementos/Screen';
import Busqueda from './Busqueda';

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    flex: 1
  },
  mensaje: {
    marginTop: 12,
    width: Dimensions.get('window').width,
    fontSize: 24,
    textAlign: 'center'
  }
});

class Buscar extends React.Component {

  static navigationOptions = {
    drawerLabel: 'Navegación',
    drawerIcon: ({ tintColor }) => <Icon name="directions-run" color={tintColor} />
  };

  mensajes = [
    "¿A dónde vas?",
    "¿Buscas un lugar en particular?",
    "¿Buscas algún producto en particular?",
    "¿Perdido? prueba buscar '¿donde estoy?'"
  ]

  render() {
    return (
      <Screen>
        <Header
          leftIcon="keyboard-arrow-left"
          leftIconAction={() => this.props.navigation.goBack()}
          title="Buscar"
        />
        <View>
          <Carousel
            data={this.mensajes}
            renderItem={({item, index})=> 
              <View>
                <Text style={styles.mensaje}>{item}</Text>
              </View>
            }
            autoplay
            autoplayDelay={2000}
            autoplayInterval={4500}
            scrollEnabled={false}
            loop
            firstItem={Math.floor(Math.random()*this.mensajes.length)}
            sliderWidth={Dimensions.get('window').width}
            itemWidth={Dimensions.get('window').width}
            inactiveSlideOpacity={0.8}
            inactiveSlideScale={0.9}
          />
          <Busqueda navigation={this.props.navigation}/>
        </View>
      </Screen>
    );
  }
}

export default Buscar;