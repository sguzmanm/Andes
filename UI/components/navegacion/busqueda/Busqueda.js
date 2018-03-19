import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, TextInput, FlatList } from 'react-native';
import { Button, Icon } from 'react-native-elements';
import Carousel from 'react-native-snap-carousel';
import Header from './../../elementos/Header';
import Screen from './../../elementos/Screen';

import machete from './machete';

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    flex: 1
  },
  input: {
    marginTop: 12,
    marginBottom: 12,
    width: "100%",
    fontSize: 24,
    textAlign: 'center'
  },
  sugerenciaContainer: {
    alignItems: 'center',
    justifyContent: 'center'
  },
  sugerenciaBorde: {
    borderBottomWidth: 2,
    borderColor: "#7303c0"
  },
  sugerencia: {
    marginTop: 4,
    marginBottom: 4,
    fontSize: 14,
    textAlign: 'center'
  }
});

class Busqueda extends React.Component {

  constructor(props){
    super(props);
    this.state = {
      busqueda: ""
    }
  }

  calcularRecomendaciones = busqueda => {
    respuesta = []
    for(lugar of machete){
      if(lugar.nombre.toLowerCase().replace(/ /g,"").includes(busqueda.toLowerCase().replace(/ /g, ""))){
        respuesta.push(lugar)
      }
    }
    return respuesta;
  }

  render() {
    const { navigation } = this.props;
    const { busqueda } = this.state;
    recomendaciones = busqueda ? this.calcularRecomendaciones(busqueda) : machete;
    return(
      <View>
        <TextInput
          style={styles.input}
          placeholder="buscar..."
          onChangeText={text => this.setState({busqueda:text})}
        />
        <FlatList
          keyExtractor={item => item.nombre}
          data={recomendaciones}
          renderItem={({item}) => 
          <View style={styles.sugerenciaContainer}>
            <TouchableOpacity style={styles.sugerenciaBorde}>
              <Text onPress={() => navigation.navigate("MapaPrincipal", {destino: item})} style={styles.sugerencia}>{item.nombre}</Text>
            </TouchableOpacity>
          </View>}
        />
      </View>
    );
  }
}

export default Busqueda;
