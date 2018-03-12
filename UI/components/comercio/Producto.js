import React from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity } from 'react-native';
import { Button, Icon } from 'react-native-elements';

const styles = StyleSheet.create({
  container: {

  },
  nombre: {
    fontSize: 16,
    textAlign: "left"
  },
  precio: {
    fontSize: 12,
    textAlign: "right"
  }
});

class Producto extends React.Component {
  render(){
    const { producto, seleccionar } = this.props;
    console.log(this.props);
    return(
      <TouchableOpacity onPress={seleccionar}>
        <View style={styles.container}>
          <Text style={styles.nombre}>
            {producto.nombre}
          </Text>
          <Text style={styles.precio}>
            {producto.precio}
          </Text>
        </View>
      </TouchableOpacity>
    );
  }
}

export default Producto;
